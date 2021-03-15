## GFT
- Insurance Assessed.zip
- Insurance_open_data.tar.gz

## TRECIS2018
- Baselines
  - Data
    - `ITR-H.types.v2.json` - Incident Tweet Routing (High-Level) Information Types
    - `nuggetsAsTweets.json.gz`
  - NotebookTest
    - ASP
    - logs
    - `HostIndex.jar`
    - `parisAttacks.[ ].`
  - SupportFiles
    - `wordembedding.categortter.map`
- Test
  - DownloadTest
  - JsonTweets
  - `event.stats`
  - `.topics`
  - `tweetids.tsv`
- Training (Datasets) 
  - CrisisLex T26 / 2013 Bohol Earthquake
  - 
- ITR-H types json
- User Profiles

## TRECIS2019A

- Assessment
- Test
  - json/csv files for various events

## TRECIS2019B

- Analysis
- Assessments
- tweets
- critical/high.tweetids
- InformationType.map

## TRECIS2020A
### 2020-A
- Disaster events
  - before_Selection
  - selection
### COVID
- Pools
- dc/nyc/washington

## TRECIS2020B
>There are three tasks as described below in 2020-B, which are the same as 2020-A. For all tasks, we evaluate systems based on their categorization and prioitization performance over different event streams.

- Task 1 and 2 : We use 14 events crawled during 2020. The topic description file for each these 14 events are provided below:
- Task 3 : The overall 'event' is the COVID-19 outbreak. This is split into nine streams, each from a different location. Topics:

TOPICS 67-75 (TASK 3)

- Task 1 and 2 :For each of the aforementioned 14 events we provide a stream of Twitter tweets. You should assign categories for all tweets in each stream. The tweet stream can be downloaded in the same way as the past events using the downloader tool. In this case, the request key should be 'trecis2020-B' (no quotes).
- Task 3 : For nine locations we provide a stream of Twitter tweets. You should assign one of more of the 9 information type labels and a priority label. The request key for the nine events is 'trecis2020-B-covid' (no quotes). The number of tweets that should be downloaded is 329,717 (50k per event, downloading may take some time, mail me if you have issues).

Events / Topics
- `TRECIS-2018-2020A.topics`
- `TRECIS-2020A-covid.topics`
>The TREC-IS data is provided for a set of (currently) 48 crisis events (note topic 7 does not exist, so they are numbered 1-49) and 3 COVID locations. The metadata for each event is provided in an XML topic file that you can download directly below:

Human Annotations / Labels
- `TRECIS-2018-2020A-labels.json`
- `TRECIS-2020A-covidlabels.json`