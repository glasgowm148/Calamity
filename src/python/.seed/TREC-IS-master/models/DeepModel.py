from keras.layers import Dense
from keras.models import Sequential
from keras.utils import to_categorical
from keras.metrics import  categorical_accuracy

from sklearn.metrics import confusion_matrix
from sklearn.metrics import f1_score
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.preprocessing import scale, normalize

import numpy as np

class Model:
    def __init__(self, X, y):
        '''
        :param X: training extracted_Features
        :param y: categories
        '''
        # Scale and normalize training extracted_Features
        # self.X = scale(X)

        self.X = normalize(X, norm='l2', axis=1)

        # encode y classes to one-hot
        self.y_encoded = to_categorical(y)

        self.num_classes = len(set(y))  # number of classes (information types)
        self.features_len = len(X[0])  # number of extracted_Features.

        self.X_train, self.x_test, self.y_train, self.y_test = train_test_split(self.X, self.y_encoded,
                                                                                test_size=0.1)
        # Feature Scaling
        sc = StandardScaler()
        self.X_train = sc.fit_transform(self.X_train)
        self.x_test = sc.transform(self.x_test)

        self.model = self.simple_DeepModel()


    def simple_DeepModel(self):
        model = Sequential()
        model.add(Dense(32, activation='relu', input_dim=self.features_len))
        model.add(Dense(self.num_classes, activation='softmax'))
        model.compile(optimizer='adam',
                      loss='categorical_crossentropy',
                       metrics=[categorical_accuracy])

        model.fit(self.X_train, self.y_train, epochs=100, batch_size=100, verbose=0, shuffle=True)

        return model

    def evaluate_model(self):
        score = self.model.evaluate(self.x_test, self.y_test, batch_size=100, verbose=0)
        return score[1]

    def confusion_matrix(self):
        y_pred = self.model.predict(self.x_test)
        cm = confusion_matrix(self.y_test, y_pred)
        return cm

    def f1_score(self):
        y_pred = (np.asarray(self.model.predict(self.x_test))).round()
        return f1_score(self.y_test, y_pred, average='micro')


    def save_model(self, f_name):
        self.model.save(f_name)
