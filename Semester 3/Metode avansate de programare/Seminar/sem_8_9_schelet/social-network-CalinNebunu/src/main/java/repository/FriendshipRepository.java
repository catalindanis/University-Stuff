package repository;

import domain.Friendship;

import java.util.Optional;

/**
 * Friendships repository interface which includes the 3 essential operations
 */
public interface FriendshipRepository {

    /**
     *
     * @param friendship
     *         friendship must be not null
     * @return an {@code Optional} - null if the friendship was saved,
     *                             - the friendship (id already exists)
     * @throws IllegalArgumentException
     *             if the given friendship is null.     *
     */
    Optional<Friendship> save(Friendship friendship);

    /**
     *  removes the friendship with the specified id
     * @param friendship
     *      friendship must be not null
     * @return an {@code Optional}
     *            - null if there is no friendship,
     *            - the removed friendship, otherwise
     * @throws IllegalArgumentException
 *                   if the given friendship null.
     */
    Optional<Friendship> delete(Friendship friendship);

    /**
     *  returns all friendships with the id given as parameter
     * @param id -the id of the friendship to be returned
     *           id must not be null
     * @return all friendships with the id given as parameter
     * @throws IllegalArgumentException
     *                  if id is null.
     */
    Iterable<Friendship> findAllFriendsOf(Long id);

}