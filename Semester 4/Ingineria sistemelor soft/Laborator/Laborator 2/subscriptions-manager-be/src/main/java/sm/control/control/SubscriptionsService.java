package sm.control.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import sm.boundary.control.SubscriptionCategoryMapper;
import sm.boundary.entity.dto.ErrorMessages;
import sm.boundary.entity.dto.SubscriptionCategoryRequest;
import sm.boundary.entity.dto.SubscriptionCategoryResponse;
import sm.boundary.entity.dto.SubscriptionCategoryUpdateRequest;
import sm.control.boundary.SubscriptionsCategoryRepository;
import sm.entity.BillingType;
import sm.entity.SubscriptionCategory;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class SubscriptionsService {

    private final SubscriptionsCategoryRepository subscriptionsCategoryRepository;
    private final SubscriptionCategoryMapper subscriptionCategoryMapper;

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

        if(categoryRequest.companyName() != null && !categoryRequest.companyName().isBlank())
            subscriptionCategory.setCompanyName(categoryRequest.companyName());
        if(categoryRequest.billingType() != null)
            subscriptionCategory.setBillingType(categoryRequest.billingType());
        if(categoryRequest.price() != null)
            subscriptionCategory.setPrice(categoryRequest.price());
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<SubscriptionCategoryResponse> findAllCategories() {
        return subscriptionCategoryMapper
                .toDTOList(subscriptionsCategoryRepository.findAll().list());
    }

    public List<BillingType> findAllBillingTypes() {
        return List.of(BillingType.values());
    }
}
