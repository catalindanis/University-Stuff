package service;

import domain.*;
import repository.*;
import utils.paging.Page;
import utils.paging.Pageable;
import validation.ValidationException;
import validation.Validator;
import domain.Event;
import domain.RaceEvent;

import java.time.LocalDate;
import java.util.*;

import static utils.GraphUtils.bfs;
import static utils.GraphUtils.getShortestPathLength;

public class SocialNetworkService {

    private Repository<Long, Persoana> persoanaRepository;
    private Repository<Long, Duck> duckRepository;
    private FriendshipDBRepository friendshipRepository;
    private Repository<Long, Card> cardRepository;
    private EventDBRepository eventRepository;

    private Validator<User> userValidator;
    private Validator<Persoana> persoanaValidator;
    private Validator<Duck> duckValidator;
    private Validator<Friendship> friendshipValidator;
    private Validator<Card> cardValidator;
    private Validator<Event> eventValidator;

    private Long nextUserId;
    private Long nextFriendshipId;
    private Long nextCardId;
    private Long nextEventId;

    // Constructor
    public SocialNetworkService(Repository<Long, Persoana> persoanaRepository, Repository<Long, Duck> duckRepository, FriendshipDBRepository friendshipRepository, Repository<Long, Card> cardRepository, EventDBRepository eventRepository,
                                Validator<User> userValidator, Validator<Persoana> persoanaValidator, Validator<Duck> duckValidator, Validator<Friendship> friendshipValidator, Validator<Card> cardValidator, Validator<Event> eventValidator) {

        this.persoanaRepository = persoanaRepository;
        this.duckRepository = duckRepository;
        this.friendshipRepository = friendshipRepository;
        this.cardRepository = cardRepository;
        this.eventRepository = eventRepository;

        this.userValidator = userValidator;
        this.persoanaValidator = persoanaValidator;
        this.duckValidator = duckValidator;
        this.friendshipValidator = friendshipValidator;
        this.cardValidator = cardValidator;
        this.eventValidator = eventValidator;

        nextUserId = 1L;
        nextCardId = 1L;
        nextEventId = 1L;
        nextFriendshipId = 1L;

    }


    // --- Persoana ---

    /**
     * Creeaza, valideaza si adauga o Persoana noua in retea.
     *
     * @return User-ul (Persoana) salvat
     * @throws ValidationException daca datele nu sunt valide
     * @throws RepositoryException daca apare un conflict de ID-uri la salvare
     */
    public User addUserPersoana(String username, String email, String password, String nume, String prenume, LocalDate dataNasterii, String ocupatie, Integer nivelEmpatie)
            throws ValidationException, RepositoryException {

        Long newId = nextUserId;
        nextUserId++;

        Persoana newUser = new Persoana(username, email, password, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);
        newUser.setId(newId);
        userValidator.validate(newUser);
        persoanaValidator.validate(newUser);
        Optional<Persoana> savedUser = persoanaRepository.save(newUser);

        if (savedUser.isPresent()) {
            nextUserId--;
            throw new RepositoryException("ID conflict on save: " + newId);
        }

        return newUser;

    }

    /**
     * Actualizeaza o persoana existenta.
     *
     * @param userToUpdate Obiectul Persoana cu datele noi (ID-ul trebuie sa existe)
     * @return User-ul (Persoana) actualizat
     * @throws ValidationException daca noile date nu sunt valide
     * @throws RepositoryException daca utilizatorul cu acel ID nu exista
     */
    public User updateUserPersoana(Persoana userToUpdate)
            throws ValidationException, RepositoryException {

        userValidator.validate(userToUpdate);
        persoanaValidator.validate(userToUpdate);

        Optional<Persoana> oldUser = persoanaRepository.update(userToUpdate);

        if (oldUser.isPresent()) {
            throw new RepositoryException("Update failed: User with ID " + userToUpdate.getId() + " does not exist.");
        }

        return userToUpdate;

    }


    // --- Duck ---

    /**
     * Creeaza, valideaza si adauga o Rata (Duck) noua in retea.
     *
     * @return User-ul (Duck) salvat
     * @throws ValidationException daca datele nu sunt valide
     * @throws RepositoryException daca apare un conflict de ID-uri la salvare
     */
    public User addUserDuck(String username, String email, String password,
                            String duckType, Double viteza, Double rezistenta, Long cardId)
            throws ValidationException, RepositoryException {

        Long newId = nextUserId++;
        Duck newUser;

        if (cardRepository.findOne(cardId).isEmpty())
            throw new ValidationException("Card not found");


        if (duckType.equalsIgnoreCase("SWIMMING")) {
            newUser = new SwimmingDuck(username, email, password, viteza, rezistenta, cardId);
            newUser.setId(newId);
        } else if (duckType.equalsIgnoreCase("FLYING")) {
            newUser = new FlyingDuck(username, email, password, viteza, rezistenta, cardId);
            newUser.setId(newId);
        } else if (duckType.equalsIgnoreCase("HYBRID")) {
            newUser = new HybridDuck(username, email, password, viteza, rezistenta, cardId);
            newUser.setId(newId);
        } else {
            nextUserId--;
            throw new ValidationException("Invalid duck type: " + duckType);
        }

        userValidator.validate(newUser);
        duckValidator.validate(newUser);

        Optional<Duck> savedUser = duckRepository.save(newUser);
        if (savedUser.isPresent()) {
            nextUserId--;
            throw new RepositoryException("ID conflict on save: " + newId);
        }

        return newUser;

    }


    /**
     * Actualizeaza o rata (Duck) existenta.
     *
     * @param userToUpdate Obiectul Duck cu datele noi (ID-ul trebuie sa existe)
     * @return User-ul (Duck) actualizat
     * @throws ValidationException daca noile date nu sunt valide
     * @throws RepositoryException daca utilizatorul cu acel ID nu exista
     */
    public User updateUserDuck(Duck userToUpdate)
            throws ValidationException, RepositoryException {

        userValidator.validate(userToUpdate);
        duckValidator.validate(userToUpdate);

        Optional<Duck> oldUser = duckRepository.update(userToUpdate);

        if (oldUser.isPresent()) {
            throw new RepositoryException("Update failed: Duck with ID " + userToUpdate.getId() + " does not exist.");
        }

        return userToUpdate;

    }

    /**
     * Gaseste un utilizator dupa ID.
     * Valideaza ID-ul inainte de a apela repository-ul.
     *
     * @param id ID-ul utilizatorului cautat
     * @return User-ul gasit, sau null daca nu exista
     * @throws ValidationException daca ID-ul este invalid (null sau negativ)
     * @throws RepositoryException daca apare o eroare in stratul de repository
     */
    public User findOneUser(Long id) throws RepositoryException, ValidationException {
        if (id == null) throw new ValidationException("ID cannot be null");
        if (id < 0) throw new ValidationException("User's id cannot be negative");

        Optional<Duck> duckOpt = duckRepository.findOne(id);
        if (duckOpt.isPresent()) return duckOpt.get();

        Optional<Persoana> persoanaOpt = persoanaRepository.findOne(id);
        return persoanaOpt.orElse(null);
    }



    /**
     * Sterge un utilizator din retea dupa ID.
     * Aceasta este o operatiune tranzactionala:
     * 1. Sterge toate prieteniile asociate utilizatorului.
     * 2. Sterge utilizatorul in sine.
     *
     * @param id ID-ul utilizatorului de sters
     * @return User-ul care a fost sters
     * @throws ValidationException daca ID-ul este invalid (null sau negativ)
     * @throws RepositoryException daca un utilizator cu acel ID nu exista
     */
    public User removeUser(Long id) throws ValidationException, RepositoryException {
        User userToDelete = findOneUser(id);

        if (userToDelete == null) {
            throw new RepositoryException("Delete failed: User with ID " + id + " does not exist.");
        }

        // 1) Stergem toate prieteniile
        Iterable<Friendship> friendships = friendshipRepository.findAllFriendsOf(id);
        Set<Long> friendsOfUser = new HashSet<>();

        for (Friendship f : friendships) {
            // Alegem ID-ul celuilalt user
            Long friendId = (f.getUser1Id().equals(id)) ? f.getUser2Id() : f.getUser1Id();
            friendsOfUser.add(friendId);
        }


        for (Long friendId : friendsOfUser) {
            Long user1 = Math.min(id, friendId);
            Long user2 = Math.max(id, friendId);
            Friendship friendship = new Friendship(user1, user2);
            friendship.setId(null); // Optional, daca delete nu folosește id-ul din DB
            friendshipRepository.delete(friendship);
        }

        for (Event event : eventRepository.findAll()) {
            unsubscribeUserFromEvent(userToDelete.getId(), event.getId());
        }

        // 2) Stergem utilizatorul din repository-ul corespunzator
        if (userToDelete instanceof Duck) {
            duckRepository.delete(id);
        } else if (userToDelete instanceof Persoana) {
            persoanaRepository.delete(id);
        }

        return userToDelete;
    }


    /**
     * Returneaza o lista cu toti utilizatorii din retea.
     * (Necesar pentru a construi graful pentru algoritmul de comunitati)
     * @return O lista cu toti utilizatorii
     */
    public List<User> findAllUsers() {
        List<User> allUsers = new ArrayList<>();

        // adaugam toti Duck
        for (Duck duck : duckRepository.findAll()) {
            allUsers.add(duck);
        }

        // adaugam toti Persoana
        for (Persoana persoana : persoanaRepository.findAll()) {
            allUsers.add(persoana);
        }

        return allUsers;
    }


    // --- Friendship ---

    /**
     * Adauga o relatie de prietenie bidirectionala intre doi utilizatori.
     * Valideaza daca ambii utilizatori exista inainte de a crea legatura.
     *
     * @param id1 ID-ul primului utilizator
     * @param id2 ID-ul celui de-al doilea utilizator
     * @throws ValidationException daca ID-urile sunt invalide sau daca unul dintre utilizatori nu exista
     * @throws RepositoryException daca prietenia exista deja (aruncata de repository)
     */
    public void addFriend(Long id1, Long id2) throws RepositoryException, ValidationException {

        Long newId = nextFriendshipId++;

        if (id1.equals(id2)) {
            throw new ValidationException("User cannot be friends with themselves");
        }

        if (this.findOneUser(id1) == null) {
            throw new ValidationException("User with ID " + id1 + " does not exist.");
        }
        if (this.findOneUser(id2) == null) {
            throw new ValidationException("User with ID " + id2 + " does not exist.");
        }

        Friendship friendship = new Friendship(id1, id2);
        friendship.setId(newId);

        friendshipValidator.validate(friendship);

        Optional<Friendship> savedFriendship = friendshipRepository.save(friendship);

        if (savedFriendship.isPresent()) {
            nextFriendshipId--;
            throw new RepositoryException("Friendship already exists: " + id1 + " - " + id2);
        }

    }

    /**
     * Sterge o relatie de prietenie bidirectionala intre doi utilizatori.
     *
     * @param id1 ID-ul primului utilizator
     * @param id2 ID-ul celui de-al doilea utilizator
     * @throws ValidationException daca ID-urile sunt invalide
     * @throws RepositoryException daca prietenia nu exista (aruncata de repository)
     */
    public void removeFriend(Long id1, Long id2) throws RepositoryException, ValidationException {

        if (id1.equals(id2)) {
            throw new ValidationException("User cannot be friends with themselves");
        }

        if (this.findOneUser(id1) == null) {
            throw new ValidationException("User with ID " + id1 + " does not exist.");
        }
        if (this.findOneUser(id2) == null) {
            throw new ValidationException("User with ID " + id2 + " does not exist.");
        }

        Friendship friendship = new Friendship(id1, id2);
        friendship.setId(1L); // ID dummy
        friendshipValidator.validate(friendship);

        Optional<Friendship> deletedFriendship = friendshipRepository.delete(friendship);
        if (deletedFriendship.isEmpty()) {
            throw new RepositoryException("Friendship does not exist: " + id1 + " - " + id2);
        }


    }

    /**
     * Gaseste toti prietenii (vecinii) unui anumit utilizator.
     * Valideaza ID-ul si existenta utilizatorului inainte de a apela repository-ul.
     *
     * @param userId ID-ul utilizatorului ai carui prieteni ii cautam
     * @return Un Set (nemodificabil) cu ID-urile prietenilor utilizatorului.
     * Returneaza un Set gol daca utilizatorul nu are prieteni.
     * @throws ValidationException daca ID-ul este invalid (null, negativ) sau
     * daca utilizatorul cu acel ID nu exista.
     * @throws RepositoryException daca apare o eroare in stratul de repository.
     */
    public Set<Long> findAllFriendsOfUser(Long userId) throws ValidationException, RepositoryException {

        User user = this.findOneUser(userId);
        if (user == null) {
            throw new ValidationException("User with ID " + userId + " does not exist.");
        }

        Set<Long> friendsOfUser = new HashSet<>();
        for (Friendship f: friendshipRepository.findAllFriendsOf(userId)) {
            Long friendId = f.getUser1Id().equals(userId) ? f.getUser2Id() : f.getUser1Id();
            friendsOfUser.add(friendId);
        }

        return friendsOfUser;
    }

    /**
     * Calculeaza numarul total de comunitati (componente conexe)
     * din intreaga retea sociala.
     * O comunitate este un grup de utilizatori in care oricare doi
     * sunt conectati printr-un lant de prietenii.
     *
     * @return int - Numarul de comunitati distincte.
     * @throws RepositoryException daca apare o eroare la accesarea datelor (ex: findAllUsers).
     */
    public int getNumberOfCommunities() {

        int cnt = 0;
        Set<Long> visitedUserIds = new HashSet<>(); // Tinem minte nodurile vizitate

        for (User user : this.findAllUsers()) {
            if (!visitedUserIds.contains(user.getId())) {
                cnt++;
                bfs(user.getId(), visitedUserIds, id -> {
                    try {
                        return findAllFriendsOfUser(id);
                    } catch (ValidationException | RepositoryException e) {
                        throw new RuntimeException(e);
                    }
                }); // BFS pentru a vizita toate nodurile, folosit cu functie lambda
            }
        }

        return cnt;

    }

    /**
     * Gaseste cea mai "sociabila" comunitate, definita ca fiind
     * componenta conexa cu cel mai mare diametru (cel mai lung drum scurt).
     *
     * @return Un Set de ID-uri de utilizatori reprezentand comunitatea
     * sau un Set gol daca nu exista utilizatori.
     */
    public Set<Long> getMostSociableCommunity() {

        int maxDiameter = -1;
        Set<Long> mostSociableCommunity = Collections.emptySet();

        // 1) Calculam comunitatile (componentele conexe)
        Set<Long> visitedUserIds = new HashSet<>();

        for (User user : this.findAllUsers()) {
            if (!visitedUserIds.contains(user.getId())) {

                // 1) a) Gasim toate nodurile din noua comunitate
                Set<Long> currentCommunityNodes = new HashSet<>();
                bfs(user.getId(), currentCommunityNodes, id -> {
                    try {
                        return findAllFriendsOfUser(id);
                    } catch (ValidationException | RepositoryException e) {
                        throw new RuntimeException(e);
                    }
                });

                // 1) b) Le marcam vizitate
                visitedUserIds.addAll(currentCommunityNodes);

                // 2) Calculam diametrul comunitatii
                int currentDiameter = 0;

                // Convertim nodurile la o lista pentru a evita calculul perechilor (u, v) si (v, u) adica convertim setul intr o lista
                List<Long> communityNodesList = new ArrayList<>(currentCommunityNodes);

                for (int i = 0; i < communityNodesList.size(); i++) {
                    for (int j = i + 1; j < communityNodesList.size(); j++) {

                        Long u = communityNodesList.get(i);
                        Long v = communityNodesList.get(j);

                        // 2) a) Folosim BFS pentru cel mai scurt drum
                        int shortestPath = getShortestPathLength(u, v, id -> {
                            try {
                                return findAllFriendsOfUser(id);
                            } catch (ValidationException | RepositoryException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        // 2) b) Actualizam diametrul maxim al acestei comunitati
                        if (shortestPath > currentDiameter) {
                            currentDiameter = shortestPath;
                        }
                    }
                }

                // 3) Verificam daca diametrul comunitatii curente e cel mai mare gasit pana acum
                if (currentDiameter > maxDiameter) {
                    maxDiameter = currentDiameter;
                    mostSociableCommunity = currentCommunityNodes;
                }
            }
        }

        return mostSociableCommunity;

    }


    // --- Card ---

    /**
     * Creeaza un card nou in retea.
     *
     * @param numeCard Numele cardului
     * @param tipMembri Tipul membrilor (ex: "SwimmingDuck")
     * @return Card-ul creat
     * @throws ValidationException daca datele nu sunt valide
     * @throws RepositoryException daca apare un conflict de ID-uri
     */
    public Card createCard(String numeCard, String tipMembri) throws RepositoryException, ValidationException {

        if (numeCard == null || numeCard.trim().isBlank())
            throw new ValidationException("Card name cannot be null or blank");

        if (tipMembri == null || tipMembri.trim().isBlank())
            throw new ValidationException("Card type cannot be null or blank");

        Long newId = nextCardId;
        nextCardId++;

        Card newCard = new Card(numeCard.trim(), tipMembri.trim().toLowerCase());
        newCard.setId(newId);
        cardValidator.validate(newCard);

        Optional<Card> savedCard = cardRepository.save(newCard);

        if (savedCard.isPresent()) {
            nextCardId--;
            throw new RepositoryException("ID conflict on save: " + newId);
        }

        return newCard;
    }

    /**
     * Adds an existing Duck to an existing Card.
     * Validates the existence of both entities and the type compatibility.
     *
     * @param duckId The ID of the Duck to add
     * @param cardId The ID of the Card to add to
     * @throws ValidationException if any ID is invalid, entities don't exist,
     * or the Duck's type is incompatible with the Card's type.
     * @throws RepositoryException if an error occurs while saving the updated cardId.
     */
    public void addDuckToCard(Long duckId, Long cardId) throws RepositoryException, ValidationException {

        User user = findOneUser(duckId);
        if (user == null) {
            throw new ValidationException("User (Duck) with ID " + duckId + " does not exist.");
        }
        if (!(user instanceof Duck)) {
            throw new ValidationException("User with ID " + duckId + " is not a Duck.");
        }
        Duck duck = (Duck) user;
        Optional<Card> cardOpt = this.findCardById(cardId); // Optional<Card>
        if (cardOpt.isEmpty()) {
            throw new ValidationException("Card with ID " + cardId + " does not exist.");
        }

        Card card = cardOpt.get();

        if (!duck.getDuckType().equals(card.getTipMembri())) {
            throw new ValidationException("Duck type does not match Card type.");
        }

        if (duck.getCardId() != null) {
            throw new ValidationException("Duck is already in a Card.");
        }

        duck.setCardId(cardId);
        duckRepository.update(duck); // Salvam Duck-ul cu noul cardId

    }

    /**
     * Gaseste un cardId (grup) dupa ID.
     * Valideaza ID-ul inainte de a apela repository-ul.
     *
     * @param cardId ID-ul cardIdului cautat
     * @return Card-ul gasit (ex: Card<?>), sau null daca nu exista
     * @throws ValidationException daca ID-ul este invalid (null sau negativ)
     * @throws RepositoryException daca apare o eroare in repository
     */
    public Optional<Card> findCardById(Long cardId) throws RepositoryException, ValidationException {

        if (cardId == null) {
            throw new ValidationException("Card ID cannot be null");
        }
        if (cardId <= 0) {
            throw new ValidationException("ID must be positive");
        }

        return cardRepository.findOne(cardId);

    }

    /**
     * Returneaza o lista cu toate cardIdurile (grupurile) din retea.
     *
     * @return O lista cu toate cardIdurile (List<Card<?>>)
     */
    public Iterable<Card> findAllCards() {
        return cardRepository.findAll();
    }

    public void removeDuckFromCard(Long duckId) throws RepositoryException, ValidationException {

        User user = this.findOneUser(duckId);
        if (user == null) {
            throw new ValidationException("User (Duck) with ID " + duckId + " does not exist.");
        }
        if (!(user instanceof Duck)) {
            throw new ValidationException("User with ID " + duckId + " is not a Duck.");
        }
        Duck duck = (Duck) user;
        if (duck.getCardId() == null)
            throw new ValidationException("Duck is not in any Card.");

        duck.setCardId(null);
        duckRepository.update(duck);

    }

    public Card deleteCard(Long cardId) throws RepositoryException, ValidationException {

        Optional<Card> card = findCardById(cardId);
        if (card.isEmpty())
            throw new ValidationException("Card with ID " + cardId + " does not exist.");

        // gasim toti Duck-ii cu cardId-ul respectiv
        List<User> allUsers = findAllUsers();
        for (User user : allUsers) {
            if (user instanceof Duck) {
                Duck duck = (Duck) user;
                if (cardId.equals(duck.getCardId())) {
                    duck.setCardId(null);
                    duckRepository.update(duck);
                }
            }
        }

        cardRepository.delete(cardId);
        return card.orElse(null);

    }


    // --- Event (OBSERVER PATTERN) ---

    /**
     * Creeaza un eveniment nou (RaceEvent) si il salveaza.
     *
     * @param description descrierea evenimentului (nu poate fi null sau vid)
     * @param distantaBalize lista distantelor balizelor (nu poate fi null sau vida)
     * @return RaceEvent creat si salvat
     * @throws ValidationException daca description sau distantaBalize sunt invalide
     * @throws RepositoryException daca apare o eroare la salvare
     */
    public RaceEvent createRaceEvent(String description, List<Double> distantaBalize) throws ValidationException, RepositoryException {

        if (description == null || description.trim().isBlank()) {
            throw new ValidationException("Event description cannot be empty.");
        }
        if (distantaBalize == null || distantaBalize.isEmpty()) {
            throw new ValidationException("Event must have at least one buoy.");
        }

        Long newId = nextEventId++;
        // Trimitem balizele la constructor
        RaceEvent newEvent = new RaceEvent(description.trim(), distantaBalize);
        newEvent.setId(newId);

        eventValidator.validate(newEvent);
        eventRepository.save(newEvent);

        return newEvent;
    }

    /**
     * Selecteaza participantii folosind Programare Dinamica
     * pentru a minimiza timpul total al cursei, respectand
     * constrangerea de rezistenta.
     */
    public void selectParticipants(RaceEvent raceEvent) {

        // --- Pas 1: Filtram si Sortam Ratele ---
        // Obtinem doar ratele care pot inota (SwimmingDuck, HybridDuck)
        Iterable<User> subscribers = eventRepository.getSubscribers(raceEvent.getId()); // aici this = RaceEvent
        List<Duck> inotatori = new ArrayList<>();
        for (User user : subscribers) {
            if (user instanceof Inotator && user instanceof Duck) {
                inotatori.add((Duck) user);
            }
        }

        List<Double> distantaBalize = raceEvent.getDistantaBalize();

        int N = inotatori.size(); // Numarul total de rate inotatoare
        int M = distantaBalize.size(); // Numarul de culoare

        if (M == 0) {
            System.out.println("Nu sunt balize definite pentru aceasta cursa.");
            return;
        }
        if (N < M) {
            System.out.println("Nu sunt suficiente rate inotatoare (" + N + ") pentru a umple " + M + " culoare.");
            return;
        }

        // Sortam ratele dupa REZISTENTA (crescator),
        // pentru a respecta constrangerea
        inotatori.sort(Comparator.comparingDouble(Duck::getRezistenta));


        // --- Pas 2: Initializam Structurile DP ---

        // dp[i][j] = Timpul minim (max-ul curselor) folosind
        //            primele 'i' rate (din lista sortata)
        //            pe primele 'j' culoare (balize sortate)
        double[][] dp = new double[N + 1][M + 1];

        // Tinem minte deciziile pentru a reconstrui calea
        int[][] path = new int[N + 1][M + 1];

        // Initializam cu Infinit
        for (int i = 0; i <= N; i++) {
            for (int j = 0; j <= M; j++) {
                dp[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        // Cazul de baza: 0 rate si 0 culoare = timp 0
        for (int i = 0; i <= N; i++) {
            dp[i][0] = 0.0;
        }

        System.out.println("DEBUG: inotatori sortati: " + inotatori);

        // --- Pas 3: Rulam Algoritmul DP ---

        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= M; j++) {

                // Optiunea 1: NU folosim rata 'i'.
                // Costul este acelasi ca si cum am alege j rate din primii i-1
                double costFaraRataI = dp[i-1][j];

                // Optiunea 2: Folosim rata 'i' pe culoarul 'j'.
                // Asta e posibil doar daca i >= j
                double costCuRataI = Double.POSITIVE_INFINITY;
                if (i >= j) {
                    Duck rata = inotatori.get(i - 1);
                    double distanta = distantaBalize.get(j - 1);

                    double timpRata = (2 * distanta) / rata.getViteza();
                    costCuRataI = Math.max(dp[i - 1][j - 1], timpRata);
                }


                // Alegem minimul dintre cele doua optiuni
                if (costFaraRataI < costCuRataI) {
                    dp[i][j] = costFaraRataI;
                    path[i][j] = 0; // Am ales sa NU luam rata 'i'
                } else {
                    dp[i][j] = costCuRataI;
                    path[i][j] = 1; // Am ales sa LUAM rata 'i'
                }
            }
        }


        // --- Pas 4: Reconstruim Solutia si Afisam ---

        double timpMinim = dp[N][M];
        if (timpMinim == Double.POSITIVE_INFINITY) {
            System.out.println("Nu s-a gasit nicio solutie fezabila.");
            return;
        }

        System.out.printf("Cursa va incepe! Timpul minim estimat: %.3f secunde\n", timpMinim);

        // Reconstruim calea
        List<Duck> rateAlese = new ArrayList<>();
        int i = N, j = M;
        while (j > 0 && i > 0) {
            if (path[i][j] == 1) {
                // Daca am ales rata 'i' pentru culoarul 'j'
                rateAlese.add(inotatori.get(i - 1));
                j--; // Mergem la culoarul anterior
            }
            i--; // Mergem la rata anterioara
        }

        // Afisam in ordinea ceruta (LIFO, de la cel mai rezistent)
        for (int k = 0; k < rateAlese.size(); k++) {
            Duck rata = rateAlese.get(k);
            int indexCuloar = M - 1 - k;
            double distanta = distantaBalize.get(indexCuloar);
            double timp = (2 * distanta) / rata.getViteza();

            // Format "Duck X on lane Y: t=z secunde"
            System.out.printf("Duck %d (%s) on lane %d: t=%.3f s (Dist: %.1fm, V: %.1f, S: %.1f)\n",
                    rata.getId(),
                    rata.getUsername(),
                    indexCuloar + 1,
                    timp,
                    distanta,
                    rata.getViteza(),
                    rata.getRezistenta()
            );
        }
    }

    public void subscribeUserToEvent(Long userId, Long eventId) {

        User user = findOneUser(userId);
        if (user == null) {
            throw new ValidationException("User with ID " + userId + " does not exist.");
        }

        Optional<Event> ev = eventRepository.findOne(eventId);
        if (ev.isEmpty()) {
            throw new ValidationException("Event with ID " + eventId + " does not exist.");
        }

        eventRepository.addSubscription(eventId, userId);
    }

    public void unsubscribeUserFromEvent(Long userId, Long eventId) {

        User user = findOneUser(userId);
        if (user == null) {
            throw new ValidationException("User with ID " + userId + " does not exist.");
        }

        Optional<Event> ev = eventRepository.findOne(eventId);
        if (ev.isEmpty()) {
            throw new ValidationException("Event with ID " + eventId + " does not exist.");
        }

        eventRepository.removeSubscription(eventId, userId);
    }

    /**
     * Declanseaza un eveniment (notifica toti subscriberii).
     *
     */
    public void triggerEvent(Long eventId) throws ValidationException {

        Optional<Event> ev = eventRepository.findOne(eventId);
        if (ev.isEmpty()) {
            throw new ValidationException("Event with ID " + eventId + " does not exist.");
        }

        Event event = ev.get();

        // Daca e RaceEvent – logica specifica
        if (event instanceof RaceEvent) {
            this.selectParticipants((RaceEvent) event); // trimiti eventId la Service
        }

        // Notificam abonatii (prin DB)
        Iterable<User> subscribers = eventRepository.getSubscribers(eventId);
        for (User u : subscribers) {
            u.receiveNotification("Event '" + event.getDescriere() + "' has been triggered!");
        }
    }

    public Page<Card> getCardsOnPage(int pageNumber, int pageSize) {
        Pageable pageable = new Pageable(pageNumber, pageSize);
        return ((CardDBRepository) cardRepository).findAllOnPage(pageable);
    }

    public int getCardsCount() {
        return ((CardDBRepository) cardRepository).count();
    }

    public int getCardsNumberOfPages(int pageSize) {
        int total = getCardsCount();
        return (total + pageSize - 1) / pageSize;
    }


}

