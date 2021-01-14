// @author: Navdeep Kaur Brar
//The js file that handles the jQuery GET requests to all the html pages used in the application when serach button is clicked or 
// when the hyperlinks are clicked And also populates the data in mainPage.Scala.html.

;(function(window) {

	'use strict';
	
	//setting global class name
	document.documentElement.className = 'js';

    
 //submit function that executes on form submit when submit button is clicked in mainPage html page. it creates a web socket connection
 // with the main controller and then sends the search terms to search for tweets. The result obatined is continuously displayed.
 jQuery('#searchform').submit(function (e) {
       e.preventDefault();
        if(  $('#searchInput').val() != ''  ){
        var str ="div" + $('#searchInput').val().replace(/[^A-Za-z]/g, "");
        if($('#searchInput').val()==':)' || $('#searchInput').val()==':-)')
				{
				str = "div" +"ppppp"
				}
				if($('#searchInput').val()==':(' || $('#searchInput').val()==':-(')
				{
				str ="div" + "qqqq"
				}
        var x = str + "senti";
         var outerdiv = "<div class=results><p>  Search Terms---->";
         outerdiv += "<a target='_blank' href='http://localhost:9000/searchTerms/"+ $('#searchInput').val()+"'>"+ $('#searchInput').val()+"</a>&nbsp;&nbsp;&nbsp;&nbsp; </p><p id="+x+"></p>";
          outerdiv += "<div class='div3'><ol class='tweetArea' id="+str+"></ol></div>";
            jQuery(".div2").append(outerdiv);
        
        	var dom = "";
        	
        	var sent = "";
       //Websocket connection created
        var ws = new WebSocket('ws://localhost:9000/initSearch');

			//sending search keys
            ws.onopen = function(msg) {
                ws.send($('#searchInput').val());
            };
        	
        	//displaying websocket result
        	 ws.onmessage = function(info) {
                var information = JSON.parse(info.data);
                information.forEach(function (value) {
                                var len = parseInt(value.length);
                                sent = value.sentiment;

                      dom = "";         
                    dom += "<li>";
                    // The hyperlink for the username and location being created
                	dom += "<a target='_blank' href='http://localhost:9000/Profile/"+value.userScreenName+"'>"+value.userName+"</a>  (<a target='_blank' href='http://localhost:9000/Location/"+value.tweetLocation+"'>"+value.tweetLocation+"</a>)--->"+value.textbody;
                	var i;
                	
                	//Creating the hyperlinked hashtag entities
					for (i = 0; i < len; i++) { 
					if(i==0)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag0.substring(1)+"'>"+value.hashtag0+"</a>";
    				if(i==1)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag1.substring(1)+"'>"+value.hashtag1+"</a>";
    				if(i==2)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag2.substring(1)+"'>"+value.hashtag2+"</a>";
    				if(i==3)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag3.substring(1)+"'>"+value.hashtag3+"</a>";
    				if(i==4)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag4.substring(1)+"'>"+value.hashtag4+"</a>";
    				if(i==5)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag5.substring(1)+"'>"+value.hashtag5+"</a>";
    				if(i==6)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag6.substring(1)+"'>"+value.hashtag6+"</a>";
    				if(i==7)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag7.substring(1)+"'>"+value.hashtag7+"</a>";
    				if(i==8)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag8.substring(1)+"'>"+value.hashtag8+"</a>";
    				if(i==9)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag9.substring(1)+"'>"+value.hashtag9+"</a>";
    				if(i==10)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag10.substring(1)+"'>"+value.hashtag10+"</a>";
    				if(i==11)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag11.substring(1)+"'>"+value.hashtag11+"</a>";
    				if(i==12)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag12.substring(1)+"'>"+value.hashtag12+"</a>";
    				if(i==13)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag13.substring(1)+"'>"+value.hashtag13+"</a>";
    				if(i==14)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag14.substring(1)+"'>"+value.hashtag14+"</a>";
    				if(i==15)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag15.substring(1)+"'>"+value.hashtag15+"</a>";
    				if(i==16)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag16.substring(1)+"'>"+value.hashtag16+"</a>";
    				if(i==17)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag17.substring(1)+"'>"+value.hashtag17+"</a>";
    				if(i==18)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag18.substring(1)+"'>"+value.hashtag18+"</a>";
    				if(i==19)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag19.substring(1)+"'>"+value.hashtag19+"</a>";
    				if(i==20)
    				dom += "&nbsp;<a target='_blank' href='http://localhost:9000/hashtag/%23"+value.hashtag20.substring(1)+"'>"+value.hashtag20+"</a>";
					}
                	
                    dom += "</li><br>";
							
				if(sent == null)
				{
				sent =":-|";
				}
				if($('#searchInput').val()==':)' || $('#searchInput').val()==':-)')
				{
				sent = ":-)"
				}
				if($('#searchInput').val()==':(' || $('#searchInput').val()==':-(')
				{
				sent = ":-("
				}
				$("#"+x).text(sent);
               ;
                //appending tweets to the appropriate div identified by the searchKeys
                    jQuery("#"+value.searchKeys).append(dom);
                    jQuery("#"+value.searchKeys).animate({ scrollTop: $("#"+value.searchKeys).prop("scrollHeight")}, 500);
                   });
            };

            ws.onclose = function(msg) 
            {
                // Logic for closed connection
                console.log('Connection was closed.');
            };
            ws.error =function(err)
            {
                // Write errors to console
                console.log(err);
            }
		}
    });

})(window);