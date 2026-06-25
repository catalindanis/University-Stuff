package sm.control.control;

import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sm.boundary.control.PaymentAlertMapper;
import sm.boundary.control.SubscriptionCategoryMapper;
import sm.boundary.control.SubscriptionMapper;
import sm.boundary.entity.dto.*;
import sm.control.boundary.PaymentAlertsRepository;
import sm.control.boundary.SubscriptionsCategoryRepository;
import sm.control.boundary.SubscriptionsRepository;
import sm.control.boundary.UsersRepository;
import sm.entity.*;

import java.time.LocalDate;
import java.util.*;

@ApplicationScoped
@RequiredArgsConstructor
public class SubscriptionsService {

    private final JsonWebToken jsonWebToken;

    private final SubscriptionsCategoryRepository subscriptionsCategoryRepository;
    private final SubscriptionsRepository subscriptionsRepository;
    private final UsersRepository usersRepository;
    private final PaymentAlertsRepository paymentAlertsRepository;

    private final SubscriptionCategoryMapper subscriptionCategoryMapper;
    private final SubscriptionMapper subscriptionMapper;
    private final PaymentAlertMapper paymentAlertMapper;

    @Transactional
    public void save(SubscriptionCategoryRequest categoryRequest) {
        SubscriptionCategory subscriptionCategory = new SubscriptionCategory();

        subscriptionCategory.setCompanyName(categoryRequest.companyName());
        subscriptionCategory.setBillingType(categoryRequest.billingType());
        subscriptionCategory.setPrice(categoryRequest.price());

        subscriptionsCategoryRepository.persist(subscriptionCategory);
    }

    @Transactional
    public void update(SubscriptionCategoryUpdateRequest categoryRequest) {
        SubscriptionCategory subscriptionCategory = subscriptionsCategoryRepository
                .findByIdOptional(categoryRequest.id())
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_FOUND));

        if (categoryRequest.companyName() != null && !categoryRequest.companyName().isBlank()) {
            subscriptionCategory.setCompanyName(categoryRequest.companyName());
        }
        if (categoryRequest.billingType() != null) {
            subscriptionCategory.setBillingType(categoryRequest.billingType());
        }
        if (categoryRequest.price() != null) {
            subscriptionCategory.setPrice(categoryRequest.price());
        }
    }

    @Transactional
    public void delete(long id) {
        SubscriptionCategory subscriptionCategory = subscriptionsCategoryRepository
                .findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_FOUND));

        subscriptionsCategoryRepository.delete(subscriptionCategory);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<SubscriptionCategoryResponse> findAllCategories() {
        return subscriptionCategoryMapper
                .toDTOList(subscriptionsCategoryRepository.findAll().list());
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<BillingType> findAllBillingTypes() {
        return List.of(BillingType.values());
    }

    @Transactional
    public void save(SubscriptionRequest request) {
        Subscription subscription = new Subscription();
        subscription.setAuthor(findCurrentUser());
        subscription.setSubscriptionCategory(findSubscriptionCategory(request.subscriptionCategoryId()));
        subscription.setStartDate(request.startDate());

        subscriptionsRepository.persist(subscription);
    }

    @Transactional
    public void update(SubscriptionUpdateRequest request) {
        User user = findCurrentUser();

        Subscription subscription = subscriptionsRepository
                .findByIdOptional(request.id())
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_FOUND));

        if(!Objects.equals(subscription.getAuthor().getId(), user.getId()))
            throw new UnauthorizedException();

        if (request.subscriptionCategoryId() != null) {
            subscription.setSubscriptionCategory(findSubscriptionCategory(request.subscriptionCategoryId()));
        }
        if (request.startDate() != null) {
            subscription.setStartDate(request.startDate());
        }
    }

    @Transactional
    public void deleteSubscription(long id) {
        User user = findCurrentUser();

        Subscription subscription = subscriptionsRepository
                .findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_FOUND));

        if(!Objects.equals(subscription.getAuthor().getId(), user.getId()))
            throw new UnauthorizedException();

        subscriptionsRepository.delete(subscription);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public SubscriptionResponse findById(long id) {
        User user = findCurrentUser();

        Subscription subscription = subscriptionsRepository
                .findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_FOUND));

        if(!Objects.equals(subscription.getAuthor().getId(), user.getId()))
            throw new UnauthorizedException();

        return subscriptionMapper.toDTO(subscription);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<SubscriptionResponse> findAllSubscriptions(LocalDate startDate, Long subscriptionCategoryId) {
        User user = findCurrentUser();
        StringBuilder query = new StringBuilder("from Subscription s");
        Map<String, Object> parameters = new HashMap<>();
        List<String> clauses = new ArrayList<>();

        clauses.add("s.author = :author");
        parameters.put("author", user);

        if (startDate != null) {
            clauses.add("s.startDate = :startDate");
            parameters.put("startDate", startDate);
        }
        if (subscriptionCategoryId != null) {
            clauses.add("s.subscriptionCategory.id = :subscriptionCategoryId");
            parameters.put("subscriptionCategoryId", subscriptionCategoryId);
        }

        query.append(" where ").append(String.join(" and ", clauses));

        return subscriptionMapper.toDTOList(subscriptionsRepository.find(query.toString(), parameters).list());
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    protected Subscription findEntityById(long id) {
        User user = findCurrentUser();

        Subscription subscription = subscriptionsRepository
                .findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_FOUND));

        if(!Objects.equals(subscription.getAuthor().getId(), user.getId()))
            throw new UnauthorizedException();

        return subscription;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    protected List<Subscription> findAllSubscriptions() {
        User user = findCurrentUser();

        StringBuilder query = new StringBuilder("from Subscription s");
        Map<String, Object> parameters = new HashMap<>();
        List<String> clauses = new ArrayList<>();

        clauses.add("s.author = :author");
        parameters.put("author", user);

        query.append(" where ").append(String.join(" and ", clauses));

        return subscriptionsRepository.find(query.toString(), parameters).list();
    }

    @Transactional
    public void createPaymentAlert(PaymentAlertRequest paymentAlertRequest) {
        User user = findCurrentUser();

        PaymentAlert paymentAlert = new PaymentAlert();
        paymentAlert.setUser(user);
        paymentAlert.setSubscription(findEntityById(paymentAlertRequest.subscriptionId()));
        paymentAlert.setDate(paymentAlertRequest.date());

        paymentAlertsRepository.persist(paymentAlert);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<PaymentAlertResponse> findAllPaymentAlerts() {
        User user = findCurrentUser();

        StringBuilder query = new StringBuilder("from PaymentAlert p");
        Map<String, Object> parameters = new HashMap<>();
        List<String> clauses = new ArrayList<>();

        clauses.add("p.user = :user");
        parameters.put("user", user);

        query.append(" where ").append(String.join(" and ", clauses));

        return paymentAlertMapper.toDTOList(paymentAlertsRepository.find(query.toString(), parameters).list());
    }

    private User findCurrentUser() {
        long userId;
        try {
            userId = Long.parseLong(jsonWebToken.getSubject());
        } catch (NumberFormatException | NullPointerException e) {
            throw new NotFoundException(ErrorMessages.NOT_FOUND);
        }

        return usersRepository.findByIdOptional(userId)
                .orElseThrow(UnauthorizedException::new);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public SubscriptionCategory findSubscriptionCategory(Long id) {
        return subscriptionsCategoryRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.NOT_FOUND));
    }
}
