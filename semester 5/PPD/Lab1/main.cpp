#include <iostream>
#include <atomic>
#include <chrono>
#include <mutex>
#include <random>
#include <thread>
#include <vector>

#define RESET      "\033[0m"
#define RED        "\033[31m"
#define GREEN      "\033[32m"

constexpr int WAREHOUSES = 10;
constexpr int PRODUCT_TYPES = 5;
constexpr int THREADS = 10;
constexpr int OPS = 10000;
constexpr int MAX_MOVE = 20;

class Product {
public:
   Product(const int id, const int type, const int amount)
   : id(id), type(type), amount(amount) {}

   int id;
   int type;
   int amount;
   std::mutex mutex;
};

class Warehouse {
private:
   int id;
   std::vector<Product*> products;
   // products are of the same type if they are on the same index

public:
   Warehouse(const int id, const std::vector<Product*>& products)
      : id(id), products(products) {}

   static void consistent_lock(Product& source, Product& destination) {
      if (source.id < destination.id) {
         source.mutex.lock();
         destination.mutex.lock();
      } else {
         destination.mutex.lock();
         source.mutex.lock();
      }
   }

   static void consistent_unlock(Product& source, Product& destination) {
      if (source.id < destination.id) {
         destination.mutex.unlock();
         source.mutex.unlock();
      } else {
         source.mutex.unlock();
         destination.mutex.unlock();
   }
   }

   static bool moveProduct(const Warehouse& from, const Warehouse& to, const int amount, const int type) {
      if (from.id == to.id) return false;
      Product& source = *from.products[type];
      Product& destination = *to.products[type];

      consistent_lock(source, destination);

      if (source.amount < amount) {
         consistent_unlock(source, destination);
         return false;
      }

      source.amount -= amount;
      source.amount += amount;

      consistent_unlock(source, destination);
      return true;
   }

   static void makeLotsOfTransactions(const std::vector<Warehouse*>& all, const unsigned seed) {
      std::mt19937 rng(seed);
      for (int i = 0; i < OPS; ++i) {
         moveProduct(*all[rng() % all.size()], *all[rng() % all.size()], 1 + rng() % MAX_MOVE, rng() % PRODUCT_TYPES);
      }
   }
};

class InventoryCheck {
private:
   std::vector<Product*> products;
   const std::vector<long> initialTotals;

   [[nodiscard]] std::vector<long> totalsPerType() const {
      for (auto& product : products) product->mutex.lock();

      std::vector<long> totals(PRODUCT_TYPES, 0);
      for (const auto* p : products) totals[p->type] += p->amount;

      for (auto& product : products) product->mutex.unlock();
      return totals;
   }

public:
   explicit InventoryCheck(const std::vector<Product*>& products)
      : products(products), initialTotals(totalsPerType()) {}

   [[nodiscard]] bool checkInventory() const {
      return initialTotals == totalsPerType();
   }
};

int main() {
    std::mt19937 rng(random());

    // product id = warehouse * PRODUCT_TYPES + type
    std::vector<Product*> products;
    for (int w = 0; w < WAREHOUSES; ++w) {
       for (int t = 0; t < PRODUCT_TYPES; ++t) {
          products.push_back(new Product(w * PRODUCT_TYPES + t, t, rng() % 100));
       }
    }

    std::vector<Warehouse*> warehouses;
    for (int w = 0; w < WAREHOUSES; ++w) {
       std::vector<Product*> perWarehouse(products.begin() + w * PRODUCT_TYPES, products.begin() + (w + 1) * PRODUCT_TYPES);
       warehouses.push_back(new Warehouse(w, perWarehouse));
    }
    const InventoryCheck inventoryCheck(products);

    const auto start = std::chrono::steady_clock::now();
    std::atomic<int> running = THREADS;
    std::vector<std::thread> threads;
    for (int i = 0; i < THREADS; ++i) {
       threads.emplace_back([&, i] {
          Warehouse::makeLotsOfTransactions(warehouses, 1000 + i);
          --running;
       });
    }

    // periodic inventory checks while the workers run
    bool ok = true;
    while (running > 0) {
       if (!inventoryCheck.checkInventory()) { ok = false; break; }
       std::this_thread::sleep_for(std::chrono::milliseconds(10));
    }
    for (auto& t : threads) t.join();

    // final check once everything has ended
    if (!ok || !inventoryCheck.checkInventory()) {
       std::cout << RED << "INVENTORY DOESN'T MATCH" << RESET << std::endl;
       return EXIT_FAILURE;
    }
    const auto ms = std::chrono::duration_cast<std::chrono::milliseconds>(
       std::chrono::steady_clock::now() - start).count();
    std::cout << GREEN << "DONE" << RESET << " in " << ms << " ms" << std::endl;

    for (const auto* w : warehouses) delete w;
    for (const auto* p : products) delete p;
    return EXIT_SUCCESS;
}