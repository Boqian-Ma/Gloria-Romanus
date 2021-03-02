package unsw.gloriaromanus.classes;

public class Player {
    private Faction faction;
    private Game game;

    public Player() {
        // create a faction
    }
   

    



    // getters and setters

    public Graph getMap(){
        return game.getMap();
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public int getCurrentRound() {
        return this.game.getCurrentRound();
    }

    public void setGame(Game game) {
        this.game = game;
    }
    // getters and setters
    public Game getGame() {
        return this.game;
    }
    public Faction getFaction() {
        return this.faction;
    }

    public String getPlayerFactionName() {
        return this.faction.getFactionName();
    }

    public Player getWinner() {
        return this.game.getWinner();
    }

    public void setWinner() {
        this.game.setWinner(this);
    }
    /*
     * public void endTurn() { game.changeCurrentPlayer(); }
     */

}
