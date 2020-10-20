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
> 19.10.2020 -

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