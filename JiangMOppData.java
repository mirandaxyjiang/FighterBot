package robotWar_Summative;

public class JiangMOppData extends OppData{

	private int healthStats;
	
	/**
	 * The JiangMOppData constructor used to make records of this type.
	 * @param id				the ID number of the player's ID for this OppData record
	 * @param a					the player's avenue for this OppData
	 * @param s					the player's street for this OppData
	 * @param health			the player's health for this OppData
	 * @param healthFightStats	the amount of health lost in the fights between the players
	 */
	public JiangMOppData(int id, int a, int s, int health, int healthStats) {
		super(id, a, s, health);
		this.healthStats = healthStats;
	}

	/**
	 * Accessor method for the amount of health lost in the fights between the players
	 * @return	the number of health lost in the fights between the players
	 */
	public int getHealthStats() {
		return this.healthStats;
	}
	
	/**
	 * Modifier method to add the amount of health that was lost in the fight
	 * @param healthResult	The amount of health lost 
	 */
	public void setHealthStats(int healthResult) {
		this.healthStats += healthResult;
	}
}
