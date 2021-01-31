package features;

import models.Tweet;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class EmoticonsTweetFeature extends TweetFeature {
	
	public static Set<String> emoticons =
			new LinkedHashSet<>();
	
	static {
		String emos = ":-) :) :o) :] :3 :c) :> =] 8) =) :} :^) :っ) :-D :D 8-D" +
				" 8D x-D xD X-D XD =-D =D =-3 =3 B^D :-)) >:[ :-( :( :-c :c :-<" +
				" :っC :< :-[ :[ :{ :-|| :@ >:( :'-( :'( :'-) :') QQ D:< D: D8 D;" +
				" D= DX v.v D-': >:O :-O :O °o° °O° :O o_O o_0 o.O 8-0 :* :^* ;-)" +
				" ;) *-) *) ;-] ;] ;D ;^) :-, >:P :-P :P X-P x-p xp XP :-p :p =p" +
				" :-Þ :Þ :-b :b >:\\ >:/ :-/ :-. :/ :\\ =/ =\\ :L =L :S >.< :|" +
				" :-| :$ :-X :X :-# :# O:-) 0:-3 0:3 0:-) 0:) 0;^) >:) >;) >:-)" +
				" }:-) }:) 3:-) 3:) o/\\o ^5 >_>^ ^<_< |;-) |-O :-& :& #-) %-)" +
				" %) :-###.. :###.. <:-| ಠ_ಠ @>-->-- 5:-) ~:-\\ //0-0\\\\" +
				" *<|:-) =:o] ,:-) 7:^] <3 </3";

		emoticons.addAll(Arrays.asList(emos.split(" ")));

	}

	@Override
	// Returns if the tweet has an emoticon or not
	public boolean classify(Tweet tweet) {
		for ( String token : tweet.getTokens() ) {
			if ( emoticons.contains(token) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the number of emoticons in the tweet
	 * @param tweet -
	 * @return - returns a count
	 */
	public static double getScore(Tweet tweet) {
		double counter = 0;
		
		for ( String token : tweet.getTokens() ) {
			if ( emoticons.contains(token) ) {
				counter++;
			}
		}
		
		return counter;
	}
	
}
