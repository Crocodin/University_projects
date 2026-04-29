from tqdm import tqdm
import numpy as np
from scipy.sparse import issparse


class ANN_noTool:

    @staticmethod
    def sigmoid(x):
        return 1 / (1 + np.exp(-x))

    @staticmethod
    def relu(x):
        return np.maximum(0, x)

    def __init__(self):
        pass

    def init_weights(self, input_size, hidden_size, output_size, learning_rate=0.01, alpha=0.0001, epochs=1000):
        self.alpha  = alpha
        self.eta    = learning_rate
        self.epochs = epochs

        self.W1 = np.random.randn(input_size, hidden_size) * np.sqrt(2 / input_size)
        self.b1 = np.zeros((1, hidden_size))
        self.W2 = np.random.randn(hidden_size, output_size) * np.sqrt(2 / hidden_size)
        self.b2 = np.zeros((1, output_size))

    def forward(self, X):
        Z1 = np.dot(X, self.W1) + self.b1
        A1 = self.relu(Z1)
        Z2 = np.dot(A1, self.W2) + self.b2
        A2 = self.sigmoid(Z2)
        cache = {"Z1": Z1, "A1": A1, "Z2": Z2, "A2": A2}
        return A2, cache

    def cost(self, Y, output):
        m             = Y.shape[0]
        cross_entropy = -np.sum(Y * np.log(output + 1e-8) + (1 - Y) * np.log(1 - output + 1e-8)) / m
        l2            = (self.alpha / (2 * m)) * (np.sum(self.W1**2) + np.sum(self.W2**2))
        return np.squeeze(cross_entropy + l2)

    def backward(self, X, Y, cache):
        m  = Y.shape[0]
        A1 = cache["A1"]
        A2 = cache["A2"]

        dZ2 = A2 - Y
        dW2 = np.dot(A1.T, dZ2) / m + (self.alpha / m) * self.W2
        db2 = np.sum(dZ2, axis=0, keepdims=True) / m

        dZ1 = np.dot(dZ2, self.W2.T) * (A1 > 0)
        dW1 = np.dot(X.T, dZ1) / m + (self.alpha / m) * self.W1
        db1 = np.sum(dZ1, axis=0, keepdims=True) / m

        return {"dW1": dW1, "db1": db1, "dW2": dW2, "db2": db2}

    def update_parameters(self, grads):
        self.W1 -= self.eta * grads["dW1"]
        self.b1 -= self.eta * grads["db1"]
        self.W2 -= self.eta * grads["dW2"]
        self.b2 -= self.eta * grads["db2"]

    def train(self, X, Y, batch_size=256):
        Y = Y.reshape(-1, 1)
        m = X.shape[0]

        epoch_bar = tqdm(range(self.epochs), desc="Training", unit="epoch")

        for epoch in epoch_bar:
            indices    = np.random.permutation(m)
            epoch_cost = 0
            num_batches = 0

            for start in range(0, m, batch_size):
                batch_indices = indices[start:start + batch_size]

                X_batch = X[batch_indices]
                if issparse(X_batch):
                    X_batch = X_batch.toarray()
                Y_batch = Y[batch_indices]

                output, cache = self.forward(X_batch)
                cost          = self.cost(Y_batch, output)
                grads         = self.backward(X_batch, Y_batch, cache)
                self.update_parameters(grads)

                epoch_cost  += cost
                num_batches += 1

            avg_cost = epoch_cost / num_batches

            epoch_bar.set_postfix({"cost": f"{avg_cost:.4f}"})

    def predict(self, X):
        if issparse(X):
            X = X.toarray()
        output, _ = self.forward(X)
        return (output > 0.5).astype(int).flatten()