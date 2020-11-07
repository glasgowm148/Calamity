"""
example of using corenlp server from python
This code requires server to already be running: https://stanfordnlp.github.io/CoreNLP/corenlp-server.html
To start server:
java -mx4g -cp "*" edu.stanford.nlp.pipeline.StanfordCoreNLPServer -port 9000 -timeout 15000
To call it, e.g.:
curl --data "The man wanted to go to work." 'http://localhost:9000/?properties={%22annotators%22%3A%22tokenize%2Cssplit%2Cpos%2Cdepparse%22%2C%22outputFormat%22%3A%22conllu%22}'
other trick: use a remote server over ssh
1. on server machine, run the corenlp server
2. on local machine, run a tunnel in its own terminal:
    ssh -NL 9000:remotehost:9000
now localhost:9000 connects to remote server at 9000.  "curl http://localhost:9000 ..." should work.
"""
import requests
import sys,json

def goparse(text, props={'annotators':'tokenize,ssplit'}):
    if isinstance(text,unicode):
        text = text.encode("utf-8")
    assert isinstance(text,str)
    assert 'annotators' in props
    assert all(isinstance(v, (str,unicode)) for v in props.values())
    r = requests.post('http://localhost:9000/', params={'properties':json.dumps(props)}, data=text)
    return r.text
    # try:
    #     return json.loads(r.text)
    # except:
    #     return {'sentences':[]}

# print goparse("Based on the book, the movie is great.", {'annotators':'tokenize,ssplit,pos,depparse', 'outputFormat':'conllu'})

# print goparse("I went there to go there.", {'annotators':'tokenize,ssplit,pos,depparse', 'outputFormat':'conllu'})

# print goparse(sys.argv[1], {
#     'annotators':'tokenize,ssplit,pos,parse', 
#     'parse.model':'edu/stanford/nlp/models/srparser/englishSR.ser.gz',
#     'parse.originalDependencies': "true",
#     'outputFormat':'conllu',
# })

## {"created_at": "Thu May 30 23:26:02 +0000 2019", "id": 1134239576111771648, "id_str": "1134239576111771648", "full_text": "Tolko\u2019s Athabasca division in Slave Lake, Alberta suspends operations due to wildfire https://t.co/7QHx7hzr08", "truncated": false, "display_text_range": [0, 109], "entities": {"hashtags": [], "symbols": [], "user_mentions": [], "urls": [{"url": "https://t.co/7QHx7hzr08", "expanded_url": "http://dlvr.it/R5kxlr", "display_url": "dlvr.it/R5kxlr", "indices": [86, 109]}]}, "source": "<a href=\"https://dlvrit.com/\" rel=\"nofollow\">dlvr.it</a>", "in_reply_to_status_id": null, "in_reply_to_status_id_str": null, "in_reply_to_user_id": null, "in_reply_to_user_id_str": null, "in_reply_to_screen_name": null, "user": {"id": 40081852, "id_str": "40081852", "name": "Quesnel Cariboo Observer", "screen_name": "QuesnelNews", "location": "Quesnel, British Columbia", "description": "Where Quesnel\u2019s news begins. Follow for breaking stories, special reports, links, features and for access to local reporters. Part of\u00a0@BlackPressMedia", "url": "https://t.co/BzEJJiOkAf", "entities": {"url": {"urls": [{"url": "https://t.co/BzEJJiOkAf", "expanded_url": "https://www.quesnelobserver.com/", "display_url": "quesnelobserver.com", "indices": [0, 23]}]}, "description": {"urls": []}}, "protected": false, "followers_count": 948, "friends_count": 64, "listed_count": 59, "created_at": "Thu May 14 20:55:07 +0000 2009", "favourites_count": 0, "utc_offset": null, "time_zone": null, "geo_enabled": false, "verified": false, "statuses_count": 17563, "lang": null, "contributors_enabled": false, "is_translator": false, "is_translation_enabled": false, "profile_background_color": "C0DEED", "profile_background_image_url": "http://abs.twimg.com/images/themes/theme1/bg.png", "profile_background_image_url_https": "https://abs.twimg.com/images/themes/theme1/bg.png", "profile_background_tile": false, "profile_image_url": "http://pbs.twimg.com/profile_images/1080986429411348481/WV954qMh_normal.jpg", "profile_image_url_https": "https://pbs.twimg.com/profile_images/1080986429411348481/WV954qMh_normal.jpg", "profile_banner_url": "https://pbs.twimg.com/profile_banners/40081852/1546562266", "profile_image_extensions_alt_text": null, "profile_banner_extensions_alt_text": null, "profile_link_color": "1DA1F2", "profile_sidebar_border_color": "C0DEED", "profile_sidebar_fill_color": "DDEEF6", "profile_text_color": "333333", "profile_use_background_image": true, "has_extended_profile": false, "default_profile": true, "default_profile_image": false, "following": false, "follow_request_sent": false, "notifications": false, "translator_type": "none"}, "geo": null, "coordinates": null, "place": null, "contributors": null, "is_quote_status": false, "retweet_count": 0, "favorite_count": 0, "favorited": false, "retweeted": false, "possibly_sensitive": false, "lang": "en"}


p = goparse(sys.argv[1], {
    'annotators':'tokenize,ssplit,pos,depparse',
    'depparse.model':'edu/stanford/nlp/models/parser/nndep/english_SD.gz',
    })
p = json.loads(p)
print json.dumps(p,indent=True)

for d in sorted(p['sentences'][0]['basicDependencies'], key=lambda d: (d['governor'],d['dependent'])):
    print (u"%s -%s-> %s" % (
        d['governorGloss']+":%s" % d['governor'],
        d['dep'],
        d['dependentGloss']+":%s" % d['dependent'],
        )).encode("utf8")




