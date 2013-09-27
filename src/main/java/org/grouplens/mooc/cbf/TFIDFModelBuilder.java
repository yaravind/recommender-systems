package org.grouplens.mooc.cbf;

import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.grouplens.lenskit.core.Transient;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;
import org.grouplens.mooc.cbf.dao.ItemTagDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.TreeMultiset;

/**
 * Builder for computing {@linkplain TFIDFModel TF-IDF models} from item tag data. Each item is represented by a
 * normalized TF-IDF vector.
 * 
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class TFIDFModelBuilder implements Provider<TFIDFModel>
{
	private final transient Logger LOG = LoggerFactory.getLogger(getClass());
	private final ItemTagDAO dao;
	private Map<Long, String> idToTag;

	/**
	 * Construct a model builder. The {@link Inject} annotation on this constructor tells LensKit that it can be used to
	 * build the model builder.
	 * 
	 * @param dao The item-tag DAO. This is where the builder will get access to items and their tags.
	 *        <p>
	 *        {@link Transient} means that the provider promises that the DAO is no longer needed once the object is
	 *        built (that is, the model will not contain a reference to the DAO). This allows LensKit to configure your
	 *        recommender components properly. It's up to you to keep this promise.
	 *        </p>
	 */
	@Inject
	public TFIDFModelBuilder(@Transient ItemTagDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * This method is where the model should actually be computed. For this task, you need to modify the model builder
	 * (TFIDFModelBuilder, your modifications go in the get() method) to compute the unit-normalized TF-IDF vector for
	 * each movie in the data set. We provide the skeleton of this; TODO comments indicate where you need to implement
	 * missing pieces. When this piece is done, the model should contain a mapping of item IDs to TF-IDF vectors,
	 * normalized to unit vectors, for each item.
	 * 
	 * @return The TF-IDF model (a model of item tag vectors).
	 */
	@Override
	public TFIDFModel get()
	{
		// Build a map of tags to numeric IDs. This lets you convert tags (which are strings)
		// into long IDs that you can use as keys in a tag vector.
		Map<String, Long> tagIds = buildTagIdMap();

		// Create a vector to accumulate document frequencies for the IDF computation
		MutableSparseVector docFreq = MutableSparseVector.create(tagIds.values());
		docFreq.fill(0);

		// We now proceed in 2 stages. First, we build a TF vector for each item.
		// While we do this, we also build the DF vector.
		// We will then apply the IDF to each TF vector and normalize it to a unit vector.

		// Create a map to store the item TF vectors.
		Map<Long, MutableSparseVector> itemVectors = Maps.newHashMap();

		// Create a work vector to accumulate each item's tag vector.
		// This vector will be re-used for each item.
		MutableSparseVector work = MutableSparseVector.create(tagIds.values());

		// Iterate over the items to compute each item's vector.
		LongSet items = dao.getItemIds();
		for (long item: items)
		{
			// Reset the work vector for this item's tags.
			work.clear();

			// Now the vector is empty (all keys are 'unset').

			// TODO1 Populate the work vector with the number of times each tag is applied to this item.
			LOG.info("Item: {} Tags: {}", item, dao.getItemTags(item));
			Multiset<String> tagFrequency = TreeMultiset.create();

			for (String t: dao.getItemTags(item))
			{
				tagFrequency.add(t);
			}
			LOG.info("Frequencies {}", tagFrequency);
			for (Entry<String> entry: tagFrequency.entrySet())
			{
				Long tagId = tagIds.get(entry.getElement());
				work.set(tagId, entry.getCount());
				LOG.info("TF for [{}] is {}", entry.getElement(), entry.getCount());
			}

			assert tagFrequency.entrySet().size() == tagFrequency.elementSet().size();

			for (String s: tagFrequency.elementSet())
			{
				// TODO1 Increment the document frequency vector once for each unique tag on the item.
				Long tagId = tagIds.get(s);

				double x = docFreq.get(tagId) + 1;
				docFreq.set(tagId, x);
			}
			/*
			 * Save a shrunk copy of the vector (only storing tags that apply to this item) in our map, we'll add IDF
			 * and normalize later.
			 */
			itemVectors.put(item, work.shrinkDomain());
			// work is ready to be reset and re-used for the next item
		}

		/*
		 * Now we've seen all the items, so we have each item's TF vector and a global vector of document frequencies.
		 * Invert and log the document frequency. We can do this in-place.
		 */
		for (VectorEntry e: docFreq.fast())
		{
			// TODO1 Update this document frequency entry to be a log-IDF value
			double totalItems = itemVectors.size();
			double noOfItemsTaggedWithThisTag = docFreq.get(e.getKey());

			double inv = totalItems / noOfItemsTaggedWithThisTag;
			double idf = Math.log(inv) / Math.log(2);

			LOG.info("Total Items: {}, No of items tagged with tag [{}]: {}, IDF: {}", totalItems, idToTag.get(e.getKey()),
					noOfItemsTaggedWithThisTag, idf);

			docFreq.set(e, idf);

			LOG.info("IDF for [{}] is {}", idToTag.get(e.getKey()), docFreq.get(e.getKey()));
		}

		/*
		 * Now docFreq is a log-IDF vector. So we can use it to apply IDF to each item vector to put it in the final
		 * model. Create a map to store the final model data.
		 */
		Map<Long, SparseVector> modelData = Maps.newHashMap();
		for (Map.Entry<Long, MutableSparseVector> entry: itemVectors.entrySet())
		{
			MutableSparseVector tv = entry.getValue();
			// TODO1 Convert this vector to a TF-IDF vector
			for (VectorEntry ve: tv.fast())
			{
				double tf = ve.getValue();
				double idf = docFreq.get(ve.getKey());
				double tfIdf = tf * idf;
				tv.set(ve, tfIdf);
				LOG.info("Tag {}, TF {}, IDF {}, TF-IDF {}", ve.getKey() + " : [" + idToTag.get(ve.getKey()) + "]", tf, idf, tfIdf);
			}

			// TODO1 Normalize the TF-IDF vector to be a unit vector
			// HINT The method tv.norm() will give you the Euclidian length of the vector

			/*
			 * TO normalize a vector, calculate the length of the vector and then divide each element of the vector by
			 * length. Length is also called the norm of a vector
			 */
			double norm = tv.norm();
			tv.multiply(1 / norm);
			// Store a frozen (immutable) version of the vector in the model data.
			modelData.put(entry.getKey(), tv.freeze());
		}

		// we technically don't need the IDF vector anymore, so long as we have no new tags
		return new TFIDFModel(tagIds, modelData);
	}

	/**
	 * Build a mapping of tags to numeric IDs.
	 * 
	 * @return A mapping from tags to IDs.
	 */
	private Map<String, Long> buildTagIdMap()
	{
		// Get the universe of all tags
		Set<String> tags = dao.getTagVocabulary();
		// Allocate our new tag map
		Map<String, Long> tagIds = Maps.newHashMap();
		idToTag = Maps.newHashMap();
		long num = 1;
		for (String tag: tags)
		{
			// Map each tag to a new number.
			tagIds.put(tag, num);
			idToTag.put(num, tag);
			num = num + 1;
		}
		return tagIds;
	}
}