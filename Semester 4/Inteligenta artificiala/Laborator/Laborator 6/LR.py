import math

class MyLogisticRegression:
    def __init__(self):
        self.intercept_ = 0.0
        self.coef_ = []

    def sigmoid(self, z):
        return 1 / (1 + math.exp(-z))

    def fit(self, x, y, learningRate=0.001, noEpochs=1000):
        n_samples = len(x)
        n_features = len(x[0])
        self.coef_ = [0.0 for _ in range(n_features + 1)]

        for epoch in range(noEpochs):
            for i in range(n_samples):
                z = self.eval_linear(x[i])
                
                y_pred = self.sigmoid(z)
                
                error = y_pred - y[i]
                
                for j in range(n_features):
                    self.coef_[j] = self.coef_[j] - learningRate * error * x[i][j]
                self.coef_[n_features] = self.coef_[n_features] - learningRate * error * 1

        self.intercept_ = self.coef_[-1]
        self.coef_ = self.coef_[:-1]

    def eval_linear(self, xi):
        yi = self.coef_[-1]
        for j in range(len(xi)):
            yi += self.coef_[j] * xi[j]
        return yi

    def predict_proba(self, x):
        return [self.sigmoid(self.eval_linear(xi)) for xi in x]

    def predict(self, x, threshold=0.5):
        probabilities = self.predict_proba(x)
        return [1 if p >= threshold else 0 for p in probabilities]