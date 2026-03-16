package model;

import game.action.*;
import game.game.*;
import java.util.*;

public abstract class Player {

    // Attributes
    private String name;
    private int prestigePoints;
    private Birthday birthday; 
    
    // Collections
    private List<DevelopmentCard> purchasedCards;
    private List<DevelopmentCard> reservedCards;
    private List<Noble> nobles;
    
    // Inventory trackers
    private Map<GemType, Integer> tokens;
    private Map<GemType, Integer> bonuses;

    // Constructor
    public Player(String name, Birthday birthday) {
        this.name = name;
        this.birthday = birthday;
        this.prestigePoints = 0;
        
        this.purchasedCards = new ArrayList<>();
        this.reservedCards = new ArrayList<>();
        this.nobles = new ArrayList<>();
        
        this.tokens = new HashMap<>();
        this.bonuses = new HashMap<>();
        
        // Initialize token and bonus maps with 0 for all standard gems + gold
        for (GemType gem : GemType.values()) {
            this.tokens.put(gem, 0);
            this.bonuses.put(gem, 0);
        }
    }
    
    /**
     * Called by GameEngine when its player's turn.
     * Implemented by HumanPlayer and ComputerPlayer.
     */
    public abstract Action takeTurn(GameState gameState);

    public String getName() {
        return this.name;
    }

    public Birthday getBirthday() {
        return this.birthday;
    }

    public int getPrestigePoints() {
        return this.prestigePoints;
    }

    public void addPrestigePoints(int amountChange) {
        this.prestigePoints += amountChange;
    }

    public Map<GemType, Integer> getTokens() {
        return this.tokens;
    }

    public Map<GemType, Integer> getBonuses() {
        return this.bonuses;
    }

    public int getBonus(GemType type) {
        return this.bonuses.getOrDefault(type, 0);
    }

    public void addBonus(GemType type, int amountChange) {
        int currentBonus = this.getBonus(type);
        this.bonuses.put(type, currentBonus + amountChange);
    }

    public List<DevelopmentCard> getPurchasedCards() {
        return this.purchasedCards;
    }

    public List<DevelopmentCard> getReservedCards() {
        return this.reservedCards;
    }

    public int getTotalTokenCount() {
        int total = 0;
        for (int count : this.tokens.values()) {
            total += count;
        }
        return total;
    }

    // Checks if the player has enough tokens and bonuses to afford a card cost.
    public boolean canAfford(Map<GemType, Integer> cost) {
        int goldTokensAvailable = this.tokens.getOrDefault(GemType.GOLD, 0);
        int missingTokens = 0;

        for (Map.Entry<GemType, Integer> entry : cost.entrySet()) {
            GemType requiredGem = entry.getKey();
            int requiredAmount = entry.getValue();

            int playerHas = this.tokens.getOrDefault(requiredGem, 0) + this.getBonus(requiredGem);
            
            if (playerHas < requiredAmount) {
                missingTokens += (requiredAmount - playerHas);
            }
        }
        
        return goldTokensAvailable >= missingTokens;
    }

    // Adds a purchased card to the inventory and applies its points and bonuses.
    public void addPurchasedCard(DevelopmentCard card) {
        this.purchasedCards.add(card);
        this.addPrestigePoints(card.getPrestigePoints());
        this.addBonus(card.getBonus(), 1);
    }

    public void addNoble(Noble noble) {
        this.nobles.add(noble);
        this.addPrestigePoints(noble.getPrestigePoints());
    }

    public void addTokens(Map<GemType, Integer> newTokens) {
        for (Map.Entry<GemType, Integer> entry : newTokens.entrySet()) {
            GemType gem = entry.getKey();
            int amount = entry.getValue();
            this.tokens.put(gem, this.tokens.getOrDefault(gem, 0) + amount);
        }
    }

    public void removeTokens(Map<GemType, Integer> tokensToSpend) {
        for (Map.Entry<GemType, Integer> entry : tokensToSpend.entrySet()) {
            GemType gem = entry.getKey();
            int amountToSpend = entry.getValue();
            this.tokens.put(gem, this.tokens.get(gem) - amountToSpend);
        }
    }
}