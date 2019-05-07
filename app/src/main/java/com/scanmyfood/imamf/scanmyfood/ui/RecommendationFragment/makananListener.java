package com.scanmyfood.imamf.scanmyfood.ui.RecommendationFragment;

public interface makananListener {
    void onItemClick(String id, String name, String cateringName, double lat, double lng);
    void onBuyClick(String id);
}
