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
  
## Week 3
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


  ## Week 4
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

Week 6
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

### **17.11.2020**

2.5hrs

Went through the Jupyter notebooks / trecis data

### **18.11.2020**

- Tidied up my data files and linked properly.

Figured out the input for the notebook. Mismatch between PDF and input was confusing me
Should be ['cat-string'] rather than ['0','1',...] 

Exported the idStr to the feature vector. Had to use `Double.parseDouble(tweet.getIdStr()))`

tfidf is printing to console, but always 0 in the array. There was a redundant (double) cast
I removed, but the problem persists. Turned out to be because I was calling FeatureVec() before Features()
This is because tfidf is term freq x inv doc fre. 
Requires each tweet to be passed in within a loop

Setting up dummy test run using fire tweets. Used a subset of 500 tweets to save time
Claculating term frequency correctly now - tf-idf still coming throgh as 0 though

Set up a method to load multiple files in future

Playing about with `maketfidflongvector.java`

Training = topics ? 


### **19.11.2020**

`5h`

- Working in Jupyter to load the labels. 
- getting the dict_predicision, and a few other features based on tfidf pulling through now

### **20.11.2020**
- none

### **21.11.2020**

`1.5h`
- Logical Regression, Random Forest, working with the TRECIS data 

### **22.11.2020**

`10h`
- Random Forest, igel, dataframe manipulation (issues with NaN, empty cols, mismatches,etc), set up play-api to export fV to a file for now.
- Managing to make somewhat sensible predictions? 
  - Attempted to print `label` (which is one of the informationTypes), all returned 5 ('EmergingThreats') - possibly due to the small dataset provided?

### **23.11.2020**

`2.5h`
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