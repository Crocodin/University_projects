<h1>
  <img src=".readme/store.png" width="80" style="vertical-align: middle; margin-right: 10px;" alt="store">
  TankingTanks DBSM
</h1>

###  About the app
**Shop Store** is an app that manages a shop. It has a .`txt` "database" where it stors items, and it can make shoping carts for users, that can be later exported as `CVS` or `HTML`. The app has a consol UI, but also a GUI made in `QT`.

## 📷 Application Preview from QT

<p align="center">
  <img src=".readme/app_user_screenshot.png" alt="Shopt store user Screenshot" width="700" style="border-radius:10px; box-shadow:0 4px 12px rgba(0,0,0,0.15);">
</p>

## 📷 Application Preview from console

<p align="center">
  <img src=".readme/consol_screenshot.png" alt="Shopt store user Screenshot" width="350" style="border-radius:10px; box-shadow:0 4px 12px rgba(0,0,0,0.15);">
</p>

To change between consol UI and GUI you just need to uncomment some code:
```C++
//#include "Ui/Controller/controller.h"
#include "GUI/guiController.h"

int main(int argc, char *argv[]) {
	QApplication app(argc, argv);
	/// run all test
	run_all_tests();
	std::cout << "Passed all tests!\n";

	// Controller controller(50, "   ", "../Utilities/productData.txt");
	// controller.run();

	 guiController controller("../Utilities/productData.txt");
	 controller.startApplication();
	 controller.show();

	return QApplication::exec();
}
```

## 📝 Export of the shoping cart

<p align="center">
  <img src=".readme/html_expot_cartpng.png" alt="Shopt store user Screenshot" width="700" style="border-radius:10px; box-shadow:0 4px 12px rgba(0,0,0,0.15);">
</p>
