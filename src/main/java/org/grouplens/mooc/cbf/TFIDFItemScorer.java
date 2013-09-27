package org.grouplens.mooc.cbf;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.grouplens.lenskit.basic.AbstractItemScorer;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.pref.Preference;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class TFIDFItemScorer extends AbstractItemScorer
{
	private transient Logger logger = LoggerFactory.getLogger(getClass());
	private final UserEventDAO dao;
	private final TFIDFModel model;

	/**
	 * Construct a new item scorer. LensKit's dependency injector will call this constructor and provide the appropriate
	 * parameters.
	 * 
	 * @param dao The user-event DAO, so we can fetch a user's ratings when scoring items for them.
	 * @param m The precomputed model containing the item tag vectors.
	 */
	@Inject
	public TFIDFItemScorer(UserEventDAO dao, TFIDFModel m)
	{
		this.dao = dao;
		model = m;
	}

	/**
	 * Generate item scores personalized for a particular user. For the TFIDF scorer, this will prepare a user profile
	 * and compare it to item tag vectors to produce the score.
	 * <p>
	 * The heart of the recommendation process in many LensKit recommenders is the score method of the item scorer, in
	 * this case TFIDFItemScorer. Modify this method to score each item by using cosine similarity: the score for an
	 * item is the cosine between that item's tag vector and the user's profile vector.
	 * </p>
	 * 
	 * @param user The user to score for.
	 * @param output The output vector. The contract of this method is that the caller creates a vector whose possible
	 *        keys are all items that should be scored; this method fills in the scores.
	 */
	@Override
	public void score(long user, @Nonnull MutableSparseVector output)
	{
		DecimalFormat df = new DecimalFormat("0.0000");
		// Get the user's profile, which is a vector with their 'like' for each tag
		SparseVector userVector = makeUserVector(user);

		// Loop over each item requested and score it.
		// The *domain* of the output vector is the items that we are to score.
		for (VectorEntry e: output.fast(VectorEntry.State.EITHER))
		{
			// Score the item represented by 'e'.
			// Get the item vector for this item
			SparseVector iv = model.getItemVector(e.getKey());
			// TODO1 Compute the cosine of this item and the user's profile, store it in the output vector

			double numerator = iv.dot(userVector);
			double denominator = iv.norm() * userVector.norm();
			double cosineSimilarity = numerator / denominator;
			try
			{
				double rounded = df.parse(df.format(cosineSimilarity)).doubleValue();
				logger.info("Cos similarity for item {} and user {} is {}", e.getKey(), user, rounded);

				output.set(e.getKey(), rounded);
			}
			catch (ParseException e1)
			{
				logger.error("Error while parsing double", e1);
			}
		}
	}

	/**
	 * Build user profile for each query user The makeUserVector(long) method of TFIDFItemScorer takes a user ID and
	 * produces a vector representing that user's profile. For Part 1, the profile should be the sum of the item-tag
	 * vectors of all items the user has rated positively (>= 3.5 stars). Complete this method.
	 * 
	 * @param user
	 * @return
	 */
	private SparseVector makeUserVector(long user)
	{
		// Get the user's ratings
		List<Rating> userRatings = dao.getEventsForUser(user, Rating.class);
		if (userRatings == null)
		{
			// the user doesn't exist
			return SparseVector.empty();
		}

		// Create a new vector over tags to accumulate the user profile
		MutableSparseVector profile = model.newTagVector();

		// Fill it with 0's initially - they don't like anything
		profile.fill(0);

		// Iterate over the user's ratings to build their profile
		for (Rating r: userRatings)
		{
			// In LensKit, ratings are expressions of preference
			Preference p = r.getPreference();
			// We'll never have a null preference. But in LensKit, ratings can have null
			// preferences to express the user unrating an item
			if (p != null && p.getValue() >= 3.5)
			{
				logger.info("User {}, Profile {|}", p.getUserId(), profile);
				// The user likes this item!
				// TODO1 Get the item's vector and add it to the user's profile
				SparseVector itemVector = model.getItemVector(p.getItemId());
				profile.add(itemVector);
			}
		}

		// The profile is accumulated, return it.
		// It is good practice to return a frozen vector.
		return profile.freeze();
	}
}
