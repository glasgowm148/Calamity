from sklearn.model_selection import cross_val_score, StratifiedKFold

import pandas as pd
from sklearn.externals import joblib

from sklearn.metrics import accuracy_score, make_scorer
# Variables for average classification report
from models.ClassicalModels import ClassicalModel
import pathlib

originalclass = []
predictedclass = []

class ModelEvaluation:

    def __init__(self, X, y, feature_name):
        '''
        :param X: training features
        :param y: categories
        '''

        # Scale and normalize training features

        #self.X = scale(X)
        #self.X = normalize(X, norm='l2')
        self.X = X
        self.y = y
        self.feature_name = feature_name
        self.model = ClassicalModel()
        pathlib.Path('saved_objects/models/trained-models/').mkdir(parents=True, exist_ok=True)
        pathlib.Path('evaluation/evaluation-report').mkdir(parents=True, exist_ok=True)

    def classification_report_with_accuracy_score(self, y_true, y_pred):

        originalclass.extend(y_true)
        predictedclass.extend(y_pred)
        return accuracy_score(y_true, y_pred)  # return accuracy score


    def run_evaluation(self, name='default'):

        with open('evaluation/evaluation-report/performance_report_final-' + name +'.md', 'a') as f:

            f.write('------' + self.feature_name + '--------')
            f.write('\n')

            models, names = self.model.get_classical_models()

            kf = StratifiedKFold(n_splits=10, shuffle=True, random_state=42)

            entries = []
            for name, model in zip(names, models):
                scores = cross_val_score(model, self.X, self.y, scoring=make_scorer(self.classification_report_with_accuracy_score), cv=kf, verbose=1, n_jobs=-1)
                # Average values in classification report for all folds in a K-fold Cross-validation
                print(name)
                # f.write(name)
                # f.write(classification_report(originalclass, predictedclass))
                joblib.dump(model, 'saved_objects/models/trained-models/' + self.feature_name + '-' + model.__class__.__name__+ '-'+ name +'.pkl')
                for fold_idx, accuracy in enumerate(scores):
                    entries.append((name, fold_idx, accuracy))

            cv_df = pd.DataFrame(entries, columns=['model_name', 'fold_idx', 'accuracy'])
            mean_acc = cv_df.groupby(['model_name'], as_index=False, sort=False).accuracy.mean()
            f.write(str(mean_acc))
            f.write('\n\n')
            print(mean_acc)

            #-----------visualization of models and accuracies---------
            # sns.barplot(x='model_name', y = 'accuracy', data=cv_df)
            # plt.show()


def main():
    obj = ModelEvaluation(None, None, 'name')

if __name__ == '__main__':
    main()

