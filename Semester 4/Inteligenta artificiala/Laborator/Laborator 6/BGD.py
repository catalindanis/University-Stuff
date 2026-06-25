import numpy as np

class MyBGDRegression:
    def __init__(self):
        self.intercept_ = 0.0
        self.coef_ = []

    # simple batch GD
    def fit(self, x, y, learningRate=0.001, noEpochs=1000):
        n_samples = len(x)
        n_features = len(x[0])
        self.coef_ = [0.0 for _ in range(n_features + 1)]
        
        for epoch in range(noEpochs):
            gradients = [0.0 for _ in range(n_features + 1)]
            
            for i in range(n_samples):
                y_computed = self.eval(x[i])
                error = y_computed - y[i]
                
                for j in range(n_features):
                    gradients[j] += error * x[i][j]
                gradients[n_features] += error * 1
            
            for j in range(n_features + 1):
                self.coef_[j] = self.coef_[j] - learningRate * (gradients[j] / n_samples)

        self.intercept_ = self.coef_[-1]
        self.coef_ = self.coef_[:-1]

    def eval(self, xi):
        yi = self.coef_[-1]
        for j in range(len(xi)):
            yi += self.coef_[j] * xi[j]
        return yi 

    def predict(self, x):
        yComputed = [self.eval(xi) for xi in x]
        return yComputed