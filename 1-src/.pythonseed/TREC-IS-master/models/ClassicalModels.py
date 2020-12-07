
from sklearn.linear_model import LogisticRegression
from sklearn.naive_bayes import MultinomialNB
from sklearn.svm import LinearSVC, SVC
from sklearn.neural_network import MLPClassifier
from sklearn.neighbors import KNeighborsClassifier
from sklearn.gaussian_process import GaussianProcessClassifier
from sklearn.gaussian_process.kernels import RBF
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier, AdaBoostClassifier
from sklearn.naive_bayes import GaussianNB
from sklearn.discriminant_analysis import QuadraticDiscriminantAnalysis
from sklearn.ensemble import GradientBoostingClassifier
from xgboost import XGBClassifier

class ClassicalModel:

    def get_classical_models(self):
        names = ["Nearest Neighbors", "Linear SVM", "RBF SVM", "Linear SVM (squared loss)", "Logistic Regression",
                 "Decision Tree", "Random Forest", "Neural Net", "Naive Bayes", "Gradient Boost"]
        models = [
            KNeighborsClassifier(n_neighbors=5, weights='distance'),
            SVC(kernel="linear", C=0.025),
            SVC(gamma=2, C=1),
            LinearSVC(random_state=42, C=0.1, dual=True, loss='squared_hinge', penalty='l2', tol=0.0001),
            LogisticRegression(random_state=42, solver='newton-cg'),
            # GaussianProcessClassifier(1.0 * RBF(1.0), n_jobs= -1, random_state=42, warm_start=True, multi_class="one_vs_rest"),
            # GaussianProcessClassifier(1.0 * RBF(1.0), n_jobs=-1),
            DecisionTreeClassifier(max_depth=7, random_state=42, criterion='gini', min_samples_leaf=2,
                                   min_samples_split=12),
            RandomForestClassifier(max_depth=5, n_estimators=10),
            MLPClassifier(alpha=1),
            # AdaBoostClassifier(),
            GaussianNB(),
            # XGBClassifier(random_state=42, learning_rate= 0.01, n_estimators= 100, subsample=0.7500000000000001),
            GradientBoostingClassifier(learning_rate=0.01, max_depth=10, max_features=0.35000000000000003,
                                       min_samples_leaf=10, min_samples_split=6, n_estimators=100,
                                       subsample=0.7500000000000001)

            # QuadraticDiscriminantAnalysis()
        ]

        return models, names