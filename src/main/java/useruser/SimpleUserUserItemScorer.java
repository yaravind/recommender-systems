package useruser;

import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.grouplens.lenskit.basic.AbstractItemScorer;
import org.grouplens.lenskit.data.dao.ItemEventDAO;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.history.History;
import org.grouplens.lenskit.data.history.RatingVectorUserHistorySummarizer;
import org.grouplens.lenskit.data.history.UserHistory;
import org.grouplens.lenskit.scored.ScoredId;
import org.grouplens.lenskit.util.TopNScoredItemAccumulator;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;
import org.grouplens.lenskit.vectors.similarity.CosineVectorSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * User-user item scorer.
 * 
 * @author Aravind R Yarram
 */
public class SimpleUserUserItemScorer extends AbstractItemScorer
{
	private final UserEventDAO userDao;
	private final ItemEventDAO itemDao;
	private static final Logger logger = LoggerFactory.getLogger(SimpleUserUserItemScorer.class);

	@Inject
	public SimpleUserUserItemScorer(UserEventDAO udao, ItemEventDAO idao)
	{
		userDao = udao;
		itemDao = idao;
	}

	@Override
	public void score(long user, @Nonnull MutableSparseVector scores)
	{
		double userMean = getUserRatingVector(user).mean();

		// This is the loop structure to iterate over items to score
		for (VectorEntry e: scores.fast(VectorEntry.State.EITHER))
		{
			long currentItem = e.getKey();

			TopNScoredItemAccumulator acc = new TopNScoredItemAccumulator(30);
			assert acc.isEmpty();

			for (Long neighbor: getPossibleNeighborsOfCurrentUser(user, currentItem))
			{
				// calculate similarity between current user and possible neighbor
				double similarity = similarity(user, neighbor);

				logger.debug("Similarity between {} and {} is {}", user, neighbor, similarity);
				acc.put(neighbor, similarity);
			}

			// get top 30 neighbors who have rated the item
			List<ScoredId> top30Neighbours = acc.finish();

			double numerator = 0;

			for (ScoredId s: top30Neighbours)
			{
				SparseVector neighborVector = getUserRatingVector(s.getId());

				double neighborMean = neighborVector.mean();

				// get the neighbors rating of current item
				double neighborsItemRating = neighborVector.get(currentItem);

				// normalize by mean centering
				double meanCenteredRating = neighborsItemRating - neighborMean;

				// calculate weighted average
				double weightedMeanCenteredRating = meanCenteredRating * s.getScore();

				logger.trace("Weighted mean-centered sum for user {} with similarity {} is {}", s.getId(), s.getScore(),
						weightedMeanCenteredRating);

				// accumulate numerator
				numerator = numerator + weightedMeanCenteredRating;
			}

			// add up all the similarities of the top 30 neighbors of current user
			double denominator = sumOfAbsoluteSimilaritiesOfNeighbours(top30Neighbours);

			// apply formula
			double prediction = userMean + (numerator / denominator);

			logger.trace("Prediction of item {} for user {} is {} + ({} / {}) = {}", currentItem, user, userMean, numerator,
					denominator, prediction);

			// add the predicted score
			scores.set(e, prediction);
		}
	}

	/**
	 * Returns all possible neighbors of the given user (excluding self) who have rated the given item
	 * 
	 * @param ignoreUser user to be excluded i.e. ignore self
	 * @param item used to find only the users who have rated this item
	 * @return all neighbors of the given user excluding him self
	 */
	private List<Long> getPossibleNeighborsOfCurrentUser(long ignoreUser, long item)
	{
		List<Long> result = Lists.newArrayList();
		LongSet possibleNeighbors = itemDao.getUsersForItem(item);
		logger.trace("Users who rated item {} are {}", item, possibleNeighbors);
		for (long u: possibleNeighbors)
		{
			if (u != ignoreUser)
			{
				result.add(u);
			}
		}
		return result;
	}

	private double sumOfAbsoluteSimilaritiesOfNeighbours(List<ScoredId> top30Neighbours)
	{
		double result = 0;
		for (ScoredId s: top30Neighbours)
		{
			result += Math.abs(s.getScore());
		}
		return result;
	}

	private SparseVector getMeanCenteredVector(Long user)
	{
		SparseVector userVector = getUserRatingVector(user);

		MutableSparseVector mutableCopy = userVector.mutableCopy();

		logger.trace("Mean of ratings for user {} is {}", user, userVector.mean());
		mutableCopy.add(-1.0 * userVector.mean());
		return mutableCopy.immutable();
	}

	private double similarity(Long user, Long neighbor)
	{
		CosineVectorSimilarity tool = new CosineVectorSimilarity();
		double similarity = tool.similarity(getMeanCenteredVector(user), getMeanCenteredVector(neighbor));
		return similarity;
	}

	/**
	 * Get a user's rating vector.
	 * 
	 * @param user The user ID.
	 * @return The rating vector.
	 */
	private SparseVector getUserRatingVector(long user)
	{
		UserHistory<Rating> history = userDao.getEventsForUser(user, Rating.class);
		if (history == null)
		{
			history = History.forUser(user);
		}
		return RatingVectorUserHistorySummarizer.makeRatingVector(history);
	}
}