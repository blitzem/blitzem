/**
 * 
 */
package org.blitzem;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Acts as a registry for items which have names or tags.
 * 
 * @author richardnorth
 * 
 */
public class TaggedItemRegistry {

	private static TaggedItemRegistry instance;
	private final List<TaggedAndNamedItem> items = Lists.newArrayList();

	private TaggedItemRegistry() {
	}

	/**
	 * @return singleton instance of {@link TaggedItemRegistry}.
	 */
	public static synchronized TaggedItemRegistry getInstance() {
		if (instance == null) {
			instance = new TaggedItemRegistry();
		}
		return instance;
	}

	/**
	 * @param item
	 *            a new item to be stored in the registry.
	 */
	public void add(TaggedAndNamedItem item) {
		this.items.add(item);
	}

	/**
	 * Find an item in the registry which matches the name or tag provided by
	 * the user on the command line.
	 * 
	 * @param nameOrTag
	 *            the tag or name provided by the user
	 * @param clazz
	 *            the class we should be limiting results to
	 * @return
	 */
	public <T> List<T> findMatching(String nameOrTag, Class<T> clazz) {

		List<T> found = Lists.newArrayList();
		for (TaggedAndNamedItem item : items) {
			final boolean correctClass = clazz.isAssignableFrom(item.getClass());
			final boolean noNameOrTag = nameOrTag == null;
			final boolean nameMatches = item.getName().equals(nameOrTag);
			final boolean tagMatches = item.getTags().contains(nameOrTag);
			if (correctClass
					&& (noNameOrTag || nameMatches || tagMatches)) {
				found.add((T) item);
			}
		}

		return found;
	}

    /**
     * Find all items in the registry which are interested in receiving notifications
     * about the state of other items.
     *
     * @param nameOrTag the name or tag about which a notification may be raised
     * @param clazz the class of notification recipients
     * @param <T>
     * @return
     */
    public <T> List<T> findReceivesNotificationsFor(String nameOrTag, Class<T> clazz) {
        List<T> found = Lists.newArrayList();

        for (TaggedAndNamedItem item : items) {
            final boolean correctClass = clazz.isAssignableFrom(item.getClass());
            final boolean isInterested = item.getNotificationSubjects().contains(nameOrTag);

            if (correctClass && isInterested) {
                found.add((T) item);
            }
        }
        
        return found;
    }
}
