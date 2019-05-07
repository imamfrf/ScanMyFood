package com.scanmyfood.imamf.scanmyfood.ui.RecommendationFragment;

public interface RecommendationListener {
    void onItemClick(String id, String name, String cateringName, double lat, double lng, String phoneNumber);
    void onBuyClick(String id, String phoneNumber);
}
