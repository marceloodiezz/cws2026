package uo.ri.cws.application.persistence.recommendation;

import java.util.List;

import uo.ri.cws.application.persistence.Gateway;

public interface RecommendationGateway extends Gateway<RecommendationGateway.RecommendationRecord> {
    
    List<RecommendationRecord> findAvailableByRecommender(String recommenderId);
    
    public class RecommendationRecord extends Gateway.BaseRecord {
        
        public String recommenderId;
        public String recommendedId;
        public boolean usedForVoucher;
        
    }
    
}
