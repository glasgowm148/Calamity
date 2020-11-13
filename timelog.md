# Timelog

* HelpMe!
* Mark Glasgow
* 2336003g
* Dr Richard McCreadie

## Week 0
> 29.09.2020 -> 02.10.2020
> *Background reading based on project description*
> **10.5hrs**
- **29.09.2020**
  - `[2hr]` Literature review
  - `[2hr]` Initial Project config (example-project + java-play-react seed)
- **30.09.2020**
  - `[3hr]` 30m Literature review
- **01.10.2020**
  - `[2hr]` Literature review
- **02.10.2020**
  - `[1hr]` Literature review
  - `[30m]` **Initial Supervisor Meeting @ 10.30**

# Week 1
> 2.10.2020 -> 12.10.2020
> *Requirements gathering and initial architectual diagram*
> **15.5hrs**

- **06.10.2020**
  - `[8hr]` System Architecture Diagram / miro
- **07.10.2020**
  - `[1hr]` System Architecture Diagram / miro 
- **08.10.2020**
  - `[1hr]` Literature review 
- **09.10.2020**
  - `[1hr]` System Architecture Diagram / miro
- **11.10.2020**
  - `[1hr]` Trello + lit. review
- **12.10.2020**
  - `[30m]` Project administration (tidy/log)
  - `[90m]` Java Classpath troubleshooting
  - `[30m]` Second meeting with supervisor
  - `[1hr]` Retrospective and Organising next sprint
  
## Week 3
> 12.10.2020 -> 19.10.2020
>
> *Further requirements gathering. Docker, Play, VPN and Datasets Setup*
>
> **23.5hrs**

- **16.10.2020**
  - `[2hr]` **VPN** Troubleshooting
  - `[4hr]` Lit. Review
    - Refocusing efforts towards categorisation only. 
- **17.10.2020**
  - `[1hr]` *Cluster* familiarisation
    - Reading over slides and taking notes
- **18.10.2020**
  - `[3hr]` VPN Troubleshooting
    - CISCO issues, alternative methods connect but don't allow me to resolve the cluster.
      - Tried setting manually using old vpn details, using an alternative to CISCO (Shimo), using a fresh account on osx, reinstalling, etc.. 
      - Works on my partners laptop - running the same version of OSX, on the same network
  - `[2hr]` Cluster familiarisation
    - [x] Completed the idagpu quickstar example
  - `[1hr]` Docker set-up
    - Image or custom build?
  - `[3hr]` Play Framework seed and familiarisation
    - [x]  [PLAY JAVA STARTER EXAMPLE](https://developer.lightbend.com/start/?group=play&project=play-samples-play-java-starter-example)
    - [x] [Tweet Miner](https://github.com/PranavBhatia/tweet-miner)
      - Fetches using twitter4j API based on the search keyword entered by the user. Searching for tweets with respect to geolocation, hashtags, etc.  Sentiment analysis and counting words can also be performed.
  - `[3hr]`: Dataset retrieval and interpretaiton
    - Scraped what I could from `nfswebhost-richardmproject`
      - The folders within `/datasets/TRECIS/` return 404/403
      - [x] 6.14GB pulled successfully
      - [X] Merged with data available for 2020B
        - rough notes in `../data/raw/datasets/notes.md`
- **19.10.2020**
  - `[3hr]` Requirements visualisation @ miro 
  - `[30m]` **Third Supervisor meeting @ 10.30**
    - `Questions/Comments for meeting`
      -  Most suitable path seems to be developing a module for existing Event-Tracker system. Access required to determine entry/end-points I would be coding to. 
      -  Online Supervised Learning vs. Incremental Learning
      -  Why does sentiment matter in this context?
         -  Is lexicon sufficient for sentiment or should the entire system utilise ML at all points? 


  ## Week 4
> 19.10.2020 - 26.10.2020

- **19.10.2020**
  - `[3hr]` - Play framework seed
- **20.10.2020**
  - `[5hr30m]` - Dockerising an instance of play framework
- **21.10.2020**
  - `[30m]` Organisation
- **22.10.2020**
- **23.10.2020**
  - `[7hr]` Research into Akka / best method of implementation
- **24.10.2020**
- **25.10.2020**
  - `[30m]` Retrospective / Updating work-log / Trello / etc.
- **26.10.2020**

Week 6
> 02.11.2020 - 09.11.2020
> `24.5hrs`
> 
> *JDK Debugging, Feature extraction implementationa and research* 

- **02.11.2020**
- **03.11.2020**
  - `[.5hr]`
    - Poor attempt at fixing broken classpath before abandonment 
- **04.11.2020**
- **05.11.2020**
  - `[3.5hrs]`
    - Twokenize.java
    - NLP / ark-tweet-nlp
- **06.11.2020**
  - `[4hrs]`
    - 2hrs Java classpath issue
      - Uninstalled all java and reinstalled JDK8 only
    - CoreNLP NERDemo.java / Glove
- **07.11.2020**
  - `[6.5hrs]`
    - 30m HomeController.java
    - 1hr tandfordAnalysis.java
    - 1.5hr DocumentLex.java
    - 30m Python server 
    - Research into NLP Methods / Lagom
    - Organising bookmarks
- **08.11.2020**
  - `[6hrs]`
    - 4.5hrs Feature extraction
      - SentimentClassification
      - DocumentLex
      - EmoticonsTweet
    - 1.5hrs Research
- **09.11.2020**
  - `[4hrs]`
    - 1hr miro
    - 30m timelog
    - 30m supervisor meeting
    - 2.5hrs Project tidy



## Notes

- Offline classification?
  - I know the data is currently offline - but I had assumed that was for development purposes, but I came across a ML video which seperated the two
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
    - show a story and how you worked through 