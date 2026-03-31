import pandas as pd
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn import linear_model
from sklearn.metrics import mean_absolute_error, mean_squared_error
import numpy as np

class LRClass:
    def __init__(self, file_path: str, target_col: str, feature_col_one: str, feature_col_two: str, feature_cols: list):
        self._file_path = file_path
        self.target_col = target_col
        self.feature_col_one = feature_col_one
        self.feature_col_two = feature_col_two
        self.feature_cols = feature_cols

        self._df = pd.read_csv(file_path)
        self.regressor_one = None
        self.regressor_two = None
        self.regressor_three = None

    def _normalize(self, col):
        self._df = self._df.dropna(subset=[col])
        min_val = self._df[col].min()
        max_val = self._df[col].max()
        if max_val == min_val:
            print(f"Warning: column '{col}' has zero variance, skipping normalization.")
            self._df[col + '_Min'] = 0.0
        else:
            self._df[col + '_Min'] = (self._df[col] - min_val) / (max_val - min_val)

    def _train(self, X, Y):
        x_train, x_test, y_train, y_test = train_test_split(X, Y, test_size=0.2, random_state=42)
        regressor = linear_model.LinearRegression()
        regressor.fit(x_train, y_train)
        predictions = regressor.predict(x_test)
        mae = mean_absolute_error(y_test, predictions)
        rmse = mean_squared_error(y_test, predictions) ** 0.5
        return regressor, x_train, x_test, y_train, y_test, predictions, mae, rmse

    # ----------- one feature ----------------------------------
    def train_feature_one(self):
        self._normalize(self.feature_col_one)
        X = self._df[self.feature_col_one + '_Min'].values.reshape(-1, 1)
        Y = self._df[self.target_col]
        
        # all the data from the first parameter
        self.regressor_one, self.x_train_one, self.x_test_one, \
        self.y_train_one, self.y_test_one, self.predictions_one, \
        self.mae_one, self.rmse_one = self._train(X, Y)
        
        # printing the errors and the model
        print(f"[1 feature] MAE: {self.mae_one:.4f}, RMSE: {self.rmse_one:.4f}")
        w0 = self.regressor_one.intercept_
        w1 = self.regressor_one.coef_[0]
        print(f"Model: f(x) = {w0:.4f} + x * {w1:.4f}")
    
    def train_feature_two(self):
        count = np.isnan(self._df[self.feature_col_two]).sum()
        print(count)
        self._normalize(self.feature_col_two)
        X = self._df[self.feature_col_two + '_Min'].values.reshape(-1, 1)
        Y = self._df[self.target_col]
        
        # all the data from the first parameter
        self.regressor_two, self.x_train_two, self.x_test_two, \
        self.y_train_two, self.y_test_two, self.predictions_two, \
        self.mae_two, self.rmse_two = self._train(X, Y)
        
        # printing the errors and the model
        print(f"[1 feature] MAE: {self.mae_two:.4f}, RMSE: {self.rmse_two:.4f}")
        w0 = self.regressor_two.intercept_
        w1 = self.regressor_two.coef_[0]
        print(f"Model: f(x) = {w0:.4f} + x * {w1:.4f}")

    def plot_feature_one(self):
        self._plot_one_feature(
            self.regressor_one, self.x_train_one, self.y_train_one, 
            self.x_test_one, self.y_test_one, self.predictions_one,
            self.feature_col_one
        )
        
    def plot_feature_two(self):
        self._plot_one_feature(
            self.regressor_two, self.x_train_two, self.y_train_two,
            self.x_test_two, self.y_test_two, self.predictions_two,
            self.feature_col_two
        )

    def _plot_one_feature(self, regressor, x_train, y_train, x_test, y_test, predictions, feature_name):
        w0 = regressor.intercept_
        w1 = regressor.coef_[0]
        x_min, x_max = x_train.min(), x_train.max()
        xref = [x_min + i * (x_max - x_min) / 1000 for i in range(1000)]
        yref = [w0 + w1 * el for el in xref]

        fig, axes = plt.subplots(1, 2, figsize=(14, 5))

        # actual vs predicted
        axes[0].plot(y_test.values, 'ro', markersize=4, label='Actual')
        axes[0].plot(predictions, 'g^', markersize=4, label='Predicted')
        axes[0].set_xlabel('Sample index')
        axes[0].set_ylabel(self.target_col)
        axes[0].set_title('Actual vs Predicted (Test Set)')
        axes[0].legend()

        # regression line
        X_all = self._df[self.feature_col_one + '_Min'].values
        Y_all = self._df[self.target_col].values
        
        axes[1].scatter(X_all, Y_all, color='blue', label='Data Points', s=10)
        axes[1].scatter(x_test, predictions, color='green', label='Predicted Points', s=10)
        axes[1].plot(xref, yref, 'r-', linewidth=2, label='Model')
        axes[1].set_xlabel(f'{feature_name} (normalized)')
        axes[1].set_ylabel(self.target_col)
        axes[1].set_title(f'{feature_name} vs {self.target_col}')
        axes[1].legend()

        plt.tight_layout()
        plt.show()

    # ----------- two features ----------------------------------
    def train_two_features(self):
        for col in self.feature_cols:
            self._normalize(col)
            
        X = self._df[[col + '_Min' for col in self.feature_cols]]
        Y = self._df[self.target_col]
        
        self.regressor_three, self.x_train_two, self.x_test_two, \
        self.y_train_two, self.y_test_two, self.predictions_two, \
        self.mae_two, self.rmse_two = self._train(X, Y)
        
        # printing the errors and the model
        print(f"[2 features] MAE: {self.mae_two:.4f}, RMSE: {self.rmse_two:.4f}")
        w0 = self.regressor_three.intercept_
        w1, w2 = self.regressor_three.coef_
        print(f"Model: f(x) = {w0:.4f} + x1 * {w1:.4f} + x2 * {w2:.4f}")

    def plot_two_features_scatter(self):
        # 3D scatter of all data points
        X = self._df[[col + '_Min' for col in self.feature_cols]]
        Y = self._df[self.target_col]
        
        fig = plt.figure()
        ax = fig.add_subplot(111, projection='3d')
        ax.scatter(X.iloc[:, 0], X.iloc[:, 1], Y, c='b', marker='o')
        
        ax.set_xlabel(f'{self.feature_cols[0]} (norm)')
        ax.set_ylabel(f'{self.feature_cols[1]} (norm)')
        ax.set_zlabel(self.target_col)
        ax.set_title(f'{self.feature_cols[0]} & {self.feature_cols[1]} vs {self.target_col}')
        plt.show()

    def plot_two_features_predictions(self):
        # 3D scatter of actual vs predicted + regression plane
        w0 = self.regressor_three.intercept_
        w1, w2 = self.regressor_three.coef_
        col0 = self.feature_cols[0] + '_Min'
        col1 = self.feature_cols[1] + '_Min'

        x1_range = np.linspace(self.x_test_two[col0].min(), self.x_test_two[col0].max(), 50)
        x2_range = np.linspace(self.x_test_two[col1].min(), self.x_test_two[col1].max(), 50)
        x1_grid, x2_grid = np.meshgrid(x1_range, x2_range)
        y_grid = w0 + w1 * x1_grid + w2 * x2_grid

        fig = plt.figure()
        ax = fig.add_subplot(111, projection='3d')
        ax.scatter(self.x_test_two[col0], self.x_test_two[col1], self.y_test_two, c='b', marker='o', label='Actual')
        ax.scatter(self.x_test_two[col0], self.x_test_two[col1], self.predictions_two, c='g', marker='^', label='Predicted')
        ax.plot_surface(x1_grid, x2_grid, y_grid, alpha=0.3, color='red')
        ax.set_xlabel(f'{self.feature_cols[0]} (norm)')
        ax.set_ylabel(f'{self.feature_cols[1]} (norm)')
        ax.set_zlabel(self.target_col)
        ax.set_title('Predicted vs Actual (Test Set)')
        ax.legend()
        plt.show()