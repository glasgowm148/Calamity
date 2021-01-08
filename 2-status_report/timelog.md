# Timelog

* HelpMe!
* Mark Glasgow
* 2336003g
* Dr Richard McCreadie

## Week 0
> 29.09.2020 -> 02.10.2020
> *Background reading based on project description*
> **10.5h**
- **29.09.2020**
  - `[2h]` Literature review
  - `[2h]` Initial Project config (example-project + java-play-react seed)
- **30.09.2020**
  - `[3h]` 30m Literature review
- **01.10.2020**
  - `[2h]` Literature review
- **02.10.2020**
  - `[1h]` Literature review
  - `[30m]` **Initial Supervisor Meeting @ 10.30**

# Week 1
> 2.10.2020 -> 12.10.2020
> *Requirements gathering and initial architectual diagram*
> **15.5h**

- **06.10.2020**
  - `[8h]` System Architecture Diagram / miro
- **07.10.2020**
  - `[1h]` System Architecture Diagram / miro 
- **08.10.2020**
  - `[1h]` Literature review 
- **09.10.2020**
  - `[1h]` System Architecture Diagram / miro
- **11.10.2020**
  - `[1h]` Trello + lit. review
- **12.10.2020**
  - `[30m]` Project administration (tidy/log)
  - `[90m]` Java Classpath troubleshooting
  - `[30m]` Second meeting with supervisor
  - `[1h]` Retrospective and Organising next sprint
  
# Week 3
> 12.10.2020 -> 19.10.2020
>
> *Further requirements gathering. Docker, Play, VPN and Datasets Setup*
>
> **23.5h**

- **16.10.2020**
  - `[2h]` **VPN** Troubleshooting
  - `[4h]` Lit. Review
    - Refocusing efforts towards categorisation only. 
- **17.10.2020**
  - `[1h]` *Cluster* familiarisation
    - Reading over slides and taking notes
- **18.10.2020**
  - `[3h]` VPN Troubleshooting
    - CISCO issues, alternative methods connect but don't allow me to resolve the cluster.
      - Tried setting manually using old vpn details, using an alternative to CISCO (Shimo), using a fresh account on osx, reinstalling, etc.. 
      - Works on my partners laptop - running the same version of OSX, on the same network
  - `[2h]` Cluster familiarisation
    - [x] Completed the idagpu quickstar example
  - `[1h]` Docker set-up
    - Image or custom build?
  - `[3h]` Play Framework seed and familiarisation
    - [x]  [PLAY JAVA STARTER EXAMPLE](https://developer.lightbend.com/start/?group=play&project=play-samples-play-java-starter-example)
    - [x] [Tweet Miner](https://github.com/PranavBhatia/tweet-miner)
      - Fetches using twitter4j API based on the search keyword entered by the user. Searching for tweets with respect to geolocation, hashtags, etc.  Sentiment analysis and counting words can also be performed.
  - `[3h]`: Dataset retrieval and interpretaiton
    - Scraped what I could from `nfswebhost-richardmproject`
      - The folders within `/datasets/TRECIS/` return 404/403
      - [x] 6.14GB pulled successfully
      - [X] Merged with data available for 2020B
        - rough notes in `../data/raw/datasets/notes.md`
- **19.10.2020**
  - `[3h]` Requirements visualisation @ miro 
  - `[30m]` **Third Supervisor meeting @ 10.30**
    - `Questions/Comments for meeting`
      -  Most suitable path seems to be developing a module for existing Event-Tracker system. Access required to determine entry/end-points I would be coding to. 
      -  Online Supervised Learning vs. Incremental Learning
      -  Why does sentiment matter in this context?
         -  Is lexicon sufficient for sentiment or should the entire system utilise ML at all points? 


# Week 4
> 19.10.2020 - 26.10.2020

- **19.10.2020**
  - `[3h]` - Play framework seed
- **20.10.2020**
  - `[5h30m]` - Dockerising an instance of play framework
- **21.10.2020**
  - `[30m]` Organisation
- **22.10.2020**
- **23.10.2020**
  - `[7h]` Research into Akka / best method of implementation
- **24.10.2020**
- **25.10.2020**
  - `[30m]` Retrospective / Updating work-log / Trello / etc.
- **26.10.2020**

# Week 6
> 02.11.2020 - 09.11.2020
> `24.5h`
> 
> *JDK Debugging, Feature extraction implementationa and research* 

- **02.11.2020**
- **03.11.2020**
  - `[.5h]`
    - Poor attempt at fixing broken classpath before abandonment 
- **04.11.2020**
- **05.11.2020**
  - `[3.5h]`
    - Twokenize.java
    - NLP / ark-tweet-nlp
- **06.11.2020**
  - `[4h]`
    - 2h Java classpath issue
      - Uninstalled all java and reinstalled JDK8 only
    - CoreNLP NERDemo.java / Glove
- **07.11.2020**
  - `[6.5h]`
    - 30m HomeController.java
    - 1h tandfordAnalysis.java
    - 1.5h DocumentLex.java
    - 30m Python server 
    - Research into NLP Methods / Lagom
    - Organising bookmarks
- **08.11.2020**
  - `[6h]`
    - 4.5h Feature extraction
      - SentimentClassification
      - DocumentLex
      - EmoticonsTweet
    - 1.5h Research
- **09.11.2020**
  - `[4h]`
    - 1h miro
    - 30m timelog
    - 30m supervisor meeting
    - 2.5h Project tidy

# Week 7
> 09.11.2020 - 16.11.2020
>
> `34h`
> 
> *JSON Parse debugging, Feature Vectors, Play-API, Jupyter* 

- **09.11.2020** - **10.11.2020**
- **11.11.2020**
  - `[2.5h]`
    - `[2h]` IntelliJ (NumericTweetFeatures, HomeController)
    - `[.5h]` Feature Vector Research
- **12.11.2020**
  - `[2.5h]`
    - Research (JSON Parse / Feature Vectors / ML)
- **13.11.2020**
  - `[8h]`
    - `[7.5h]` IntelliJ (HomeController:JSONParse)
- **14.11.2020**
  - `[5h]`
    - `[4h]` IntelliJ (TermFrequency, JSON)
- **15.11.2020**
  - `[12h]`
    - `[10h]` IntelliJ (Feature Vector, API)
    - `[1h]` Jupyter Notebook Setup
    - `[1h]` Play API Research
- **16.11.2020**
  - `[4h]`
    - `[2.5h]` Jupyter Linear Regression
    - `[1.5h]` Meeting prep / Meeting

> Questions
> 1. Does the feature vector look appropriate?
> 2. Why are we using Play to do the pre-processing / at all?

> Next Steps
> 1. Linear Regression 
> 2. Debugging API / ndJsonParse
> 3. Refining Feature Vector

> Existing issues
> 1. ndjson - Reading in a % of the tweets, tripping up on some. 
> 2. Can't access the vector statically, need to tidy Akka
> 3. Misc low priority issues (logging, refleciton, etc)

# Week 9
> 17.11.2020 - 23.11.2020
> `~40h` + `5h` (research methods & techniques)
> *Dataframe prep and classification attempts* 

- **17.11.2020** `2.5h`

-Went through the Jupyter notebooks / trecis data
- **18.11.2020** `12hr`
  - Tidied up my data files and linked properly.
  - TRECIS output format replication
  - idStr output from java
  - feature vector tidying / expanding with better tfidf values (HAN library)
  - multiple files in java method
- **19.11.2020** `5h`
  - Working in Jupyter to load the labels. 
  - getting the dict_predicision, and a few other features based on tfidf pulling through now
- **21.11.2020** `1.5h`
  - Logical Regression, Random Forest, working with the TRECIS data 
- **22.11.2020** `13h`
  - Random Forest, igel, dataframe manipulation (issues with NaN, empty cols, mismatches,etc), set up play-api to export fV to a file for now.
  - Managing to make somewhat sensible predictions? 
    - Attempted to print `label` (which is one of the informationTypes), all returned 5 ('EmergingThreats') - possibly due to the small dataset provided?
- **23.11.2020** `2.5h`
- Tidying up notebooks, stuck on dummy_data currently
- supervisor meeting
- tidying work-log
## Status:: 
> Starting to make sense, but still not entirely sure how to split/re-join the data. 

Currently I'm thinking
1. Get feature_vector `test` of `before_selection`
2. Get feature vector `train` of `assr*.json` + `labels/topics`
3. concat + get_dummies 
4. ML algorithms
5. fit / evaluate on `train`
6. predict on `test`
7. merge predictions to `test`
8. export `test` to TRECIS output format 

src/python/notebooks/model_results contains results for various ML algorithms

 "results_on_test_data": {
        "accuracy_score": 0.24867724867724866,
        "f1_score": 0.24867724867724866,
        "precision_score": 0.24867724867724866,
        "recall_score": 0.24867724867724866
    },

# Week 10
> 23.11.2020 - 30.11.2020



# Week 11
> 17.11.2020 - 7.12.2020
> `xh`

- 05.12.20
  - Tidied workspace 
  - Got eventID and the labels in the proper format.
    - Returning it through the evaluation script returns additional numbers 
- 06.12.20
  - Creating plan for next 2 weeks
  - Tidying up timelogs
- 07.12.20
  - Supervisor meeting

## Plan for the next two weeks

Current process
1. [x] Extract feature vector from `before_selection`
2. [x] Merge with `High-information-types` to get the `eventID`, `
3. [x] Save two dataframes, one complete and one with the columns you want to predict removed. 
  - [x] Predict `Priority`
  - [x] Predict `Information-Types`
4. [x] Merge predictions back into dataframes
5. [x] Export in `TRECIS.run` format

Next Steps
1. [x] Read all files at once
2. [x] Pass through eventID (?) merging labelled data with selection.json ? 
3. [x] redict High Level Information Types


# Week 12
> 7.12.2020 - 14.12.2020
> `xh`

- 7.12.20
- 8.12.20
- 9.12.20
  - Java/Play
  - Loading all tweets.
  - Deserialisation
  - Predicting multi-labels
- 10.12.20
  - Count/Trim labels
- 11.12.20
- 12.12.20
  - event
- 13.12.20 (11am-7pm)
  - long int bug
  - work on labels
- 14.12.20 (8am-11)
  - Monday. Supervisor meeting
  - Tidy/prep
  - Seaborne
  - Merging labels into myrun.ipynb


```bash
P = Priority
T = Type
                              P          P              T         T        T         P        P
Run             & NDCG    & AW-H      & AW-A      & CF1-H   & CF1-A   & CAcc   & PErr-H & PErr-A \\
marksrun2       & 0.6179  & -0.1551   & -0.0777   & 0.0718  & 0.1085  & 0.9265 & 0.1632 & 0.3297 \\ All Tweets first run (Priority + 1 label)
marksrun2       & 0.5446  & -0.1459   & -0.0731   & 0.1031  & 0.1664  & 0.9001 & 0.1430 & 0.2145 \\ Multiple labels
marksrun2       & 0.5452  & -0.1459   & -0.0731   & 0.1009  & 0.1890  & 0.9203 & 0.1430 & 0.2148 \\ Predicting number of labels
marksrun2       & 0.5440  & -0.1474   & -0.0739   & 0.1090  & 0.1872  & 0.9201 & 0.1569 & 0.2731 \\ priority_y -> x
marksrun2       & 0.5418  & -0.1505   & -0.0754   & 0.1151  & 0.1935  & 0.9203 & 0.1570 & 0.2693 \\ label/eventID tidy
marksrun2       & 0.5538  & -0.1505   & -0.0754   & 0.1063  & 0.1954  & 0.9213 & 0.1565 & 0.2715 \\ eventID passing properly
marksrun2       & 0.5825  & -0.1459   & -0.0731   & 0.1284  & 0.2172  & 0.9259 & 0.1549 & 0.2770 \\ labels passing properly
marksrun2       & 0.6201  & -0.1459   & -0.0731   & 0.1284  & 0.2172  & 0.9259 & 0.1591 & 0.2967 \\
marksrun2       & 0.6088  & -0.2288   & -0.1144   & 0.1284  & 0.2172  & 0.9259 & 0.1621 & 0.3133 \\
marksrun2       & 0.5527  & -0.2765   & -0.1382   & 0.0279  & 0.0645  & 0.9122 & 0.1125 & 0.2366 \\ tidying input labels df
marksrun2       & 0.5774  & -0.2734   & -0.1367   & 0.0373  & 0.0677  & 0.9127 & 0.1056 & 0.2365 \\ rerun of above
marksrun2       & 0.5823  & -0.2119   & -0.1061   & 0.0308  & 0.0657  & 0.9131 & 0.1207 & 0.2592 \\
marksrun2       & 0.5802  & -0.2135   & -0.1068   & 0.0357  & 0.0669  & 0.9136 & 0.1154 & 0.2482 \\
marksrun2       & 0.5791  & -0.2165   & -0.1083   & 0.0313  & 0.0671  & 0.9128 & 0.1155 & 0.2534 \\ set scale in mutli.yaml
marksrun2       & 0.5733  & -0.2181   & -0.1091   & 0.0331  & 0.0656  & 0.9128 & 0.1180 & 0.2605 \\ set scale all in hyper.yaml
marksrun2       & 0.5800  & -0.2227   & -0.1114   & 0.0391  & 0.0669  & 0.9132 & 0.1112 & 0.2491 \\ LinearSVM reg
marksrun2       & 0.5978  & -0.1551   & -0.0779   & 0.1731  & 0.2327  & 0.9242 & 0.1743 & 0.3163 \\ Decision Tree
marksrun2       & 0.5838  & -0.2104   & -0.1053   & 0.0295  & 0.0633  & 0.9125 & 0.1196 & 0.2551 \\ MultinomialNaiveBayes reg
marksrun2       & 0.3674  & -0.1785   & -0.0892   & 0.0225  & 0.0277  & 0.9107 & 0.0550 & 0.1544 \\ Bayer class + reg
labeldata       & 0.5882  & -0.1689   & -0.0846   & 0.1490  & 0.2184  & 0.9241 & 0.1468 & 0.2904 \\
marksrun2       & 0.5892  & -0.1612   & -0.0808   & 0.1452  & 0.2225  & 0.9246 & 0.1508 & 0.3074 \\ long int fix - 6k labels
marksrun2       & 0.5902  & -0.1628   & -0.0816   & 0.1725  & 0.2157  & 0.9236 & 0.1626 & 0.3089 \\ long int fix - 42k labels - 3k tweets
marksrun2       & 0.6267  & -0.1459   & -0.0731   & 0.1695  & 0.2283  & 0.9241 & 0.1685 & 0.3346 \\ added assr data to labels ? 
marksrun2       & 0.4943  & -0.2411   & -0.1228   & 0.1490  & 0.2296  & 0.9242 & 0.1142 & 0.2639 \\ added labels load to myrun
marksrun2       & 0.5010  & -0.2350   & -0.1193   & 0.1811  & 0.2526  & 0.9260 & 0.1337 & 0.2694 \\ turned off group by tweet
marksrun2       & 0.5079  & -0.2335   & -0.1186   & 0.1859  & 0.2550  & 0.9262 & 0.1409 & 0.2726 \\ rerun
marksrun2       & 0.5014  & -0.2350   & -0.1193   & 0.1843  & 0.2544  & 0.9260 & 0.1399 & 0.2762 \\
marksrun2       & 0.5034  & -0.2350   & -0.1193   & 0.0671  & 0.1213  & 0.9205 & 0.1368 & 0.2663 \\ RandomForest
marksrun2       & 0.5064  & -0.2335   & -0.1185   & 0.1834  & 0.2549  & 0.9260 & 0.1357 & 0.2746              \\ DecisionTree + RandomForest
marksrun2       & 0.3682  & -0.0757   & -0.0378   & 0.0098  & 0.0148  & 0.9212 & 0.0669 & 0.1544 \\ LinearRegression


njit-sub01.text & 0.4632  & -0.4801   & -0.2493   & 0.0792  & 0.1582  & 0.9025 & 0.1524 & 0.2198 \\ 

```


NDCG    =   `system_ndcg_micro`                               =   System Event-Micro nDCG (Normalized Discounted Cumulative Gain)  - Calculates `usefulness/relevancy` 
AW-H    =   `totalHighImportWorth / countHighCriticalImport`  =   Count of the number of High Priority tweets
AW-A    =   `AccumulatedAlertWorth`                           =   Measures system effectiveness from the perspective of end-user alerting of important information
CF1-H   =   `avgF1High / numHighInformationTypes`             =   Precision on the High Information Types
CF1-A   =   `avgF1 / numInformationTypes`                     =   Information Type F1 (positive class, multi-type, macro)
CAcc    =   `avgAccuracy / numInformationTypes`               =   Information Type Accuracy (overall, multi-type, macro)
PErr-H  =   `priorityAvgf1High / numHighInformationTypes`     =   How divergent is the system from the High-level human priority labels?
PErr-A  =   `priorityAvgf1 / len(informationTypes2Index`      =   How divergent is the system from all human priority labels?


## Notes

- 2019B most suitable data source?
  - analysis
    - critical.tweets.json.gz
    - critical.tweets.uniq.json.gz
    - high.tweets...
    - ..
  - tweets
    - alberta_wildfires
    - colorado_stem_shooting
      - colorado_stem_shooting_aa
      - colorado_stem_shooting_aa.gz
      - colorado_stem_shooting_ab
      - colorado_stem_shooting_ab.gz
      - ...
  - assessment
    - 2019B-assr1.json
    - ...
  - Critical.tweetid
  - InformationType.mapping
  - ITR-H.types.v3.json
  - nyu-smapp_run_fasttext_multi.csv






## Notes


- 50k terms, exist or not? 
  - not binary exist or not instead to a TDIF
    - does it exist
    - embedding force
      - Triple TVEC
      - BERT 
      - Muppet Models ? 

- Classical vs Deep Learning
  - CPU vs GPU
  - CPU : Fewer parameters, linear regression - learning a linear weight to your features
  - Tree based learning 
  - Deep : Blackbox, 
  - deep to generate features
  - deep end-to-end, neural networks does that classification

- Bert -> Vector of numbers -> Linear Regression 

- 99% wont be relevent for search and recue
  - Accuracy not that important
  - Performance - recall is most important (positive class calculation / balanced accuracy)
  - cant afford to miss relevent stuff, rather more spam 


- How it sources it categorisation data.  
  - Pull from my system instead 
  - API or Empty Mailbox
  - 2 step process
  - get historical data
  - auto adds it to the listing 
  
- Just start with offline learning
  - Feedback to update learning
  - Debug U.I 

- Sentiment Analysis
  - Solution for category 

- Sentiment Analysis using ML 
  - grab a library 

- For next week, pre-processing script 
  - see performance
  - see where it fails
  - add / improvimng structure story
    - show a story and how you worked though 


# Week 1x
> 16.12.2020 - 11.01.2021
- GloVe embeddings
- Offset

## ToDo
- BERTweet - https://github.com/VinAIResearch/BERTweet#usage2
- http://www.cs.cmu.edu/~ark/TweetNLP/
- https://cloud.google.com/natural-language/automl/docs/tutorial
- https://github.com/kbastani/sentiment-analysis-twitter-microservices-example
- https://github.com/millecker/senti-storm
- Actor system