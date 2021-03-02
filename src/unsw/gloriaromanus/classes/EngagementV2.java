package unsw.gloriaromanus.classes;

import java.util.ArrayList;
import java.util.Random;

public class EngagementV2 {
    
    private Unit attackUnit;
    private Unit defendUnit;
    private BattleV2 battle;
    private String engagementType;

    private SkirmishV2 skirmish;

    private boolean attackUnitBreak;
    private boolean defendUnitBreak;

    private boolean attackUnitRoute;
   

    public EngagementV2(Unit attackUnit, Unit defendUnit, BattleV2 battle, SkirmishV2 skirmish) {
        this.attackUnit = attackUnit;
        this.defendUnit = defendUnit;
        this.battle = battle;
        this.engagementType = determineType(attackUnit, defendUnit);
        this.attackUnitBreak = false;
        this.defendUnitBreak = false;
        this.skirmish = skirmish;
        this.attackUnitRoute = false;
    }


    public void engage() {
        
        this.determineType();
        //Unit routing = null;

        // while two units both have remaining
        while (this.attackUnit.getNumTroops() > 0 && this.defendUnit.getNumTroops() > 0) {
            if (this.attackUnitBreak == true || this.defendUnitBreak == true) {
                break;
            } 
            int attackBefore = attackUnit.getNumTroops();
            int defendBefore = defendUnit.getNumTroops();

            // attack units engage conflict and reduce number
            int attackingCas = this.attackingTurn();

            if (this.defendUnit.getNumTroops() - attackingCas < 0) {
                this.defendUnit.setNumTroops(0);
            } else{
                this.defendUnit.setNumTroops(defendBefore - attackingCas);
            }

            int defendingCas = this.defendingTurn();
            if (this.attackUnit.getNumTroops() - defendingCas < 0) {
                this.attackUnit.setNumTroops(0);
            } else{
                this.attackUnit.setNumTroops(attackBefore - defendingCas);
            }

            // find if a unit breaks
            
            if ((this.defendUnit.getNumTroops() > 0 && this.attackUnit.getNumTroops() > 0)) {
                breaking(attackingCas, defendingCas);
            }
            // add to an engagement
            this.battle.addNumEngagement();



            System.out.println("god damn ");
            System.out.println("Attack unit " + this.attackUnit.getNumTroops());
            System.out.println("Attack casualty " + attackingCas);
            System.out.println("defend unit " + this.defendUnit.getNumTroops());
            System.out.println("Defend casualty " + defendingCas);
        }


        // no breaking cases
        if (attackUnitBreak == false && defendUnitBreak == false) {
            System.out.println("***no breaking case");
            // if attack unit has left over, attack unit returns
            if (attackUnit.getNumTroops() > 0 && defendUnit.getNumTroops() == 0) {
                // attack unit returns to army
                battle.addAttackUnit(attackUnit);
                return;
            } 

            // if defend unit wins, this unit returns back to defend
            if (attackUnit.getNumTroops() == 0 && defendUnit.getNumTroops() > 0) {
                // defend unit returns to army
                battle.addDefendUnit(defendUnit);
                return;
            } 

        }



        // if defending unit breaks, engagement automatically won by attacking unit
        // or if defending unit loses
        // attcking unit returns to army to continue battles
        if (this.defendUnitBreak == true || (this.defendUnit.getNumTroops() == 0 && this.attackUnit.getNumTroops() > 0)) {
            // attackunit returns to army
            System.out.println("defending unit breaks");
            battle.addAttackUnit(attackUnit);
        }

        // if both dies
        if ((this.defendUnit.getNumTroops() == 0 && this.attackUnit.getNumTroops() == 0)) {

            System.out.println("both die at the same time");
            return;
        }

        if (this.attackUnitBreak == true) {
            // if attack unit breaks
            while (!this.attackUnitRoute && this.attackUnit.getNumTroops() > 0) {
                // while attack unit hasnt routed
                System.out.println("attampred to rouytred");
                System.out.println("unit troop = " + this.attackUnit.getNumTroops());
                this.route();
                // if routed
                if (this.attackUnitRoute) {
                    // if attack unit routed, go back to province
                    break;
                }
                // inflict casualty, only 
                int attackBefore = attackUnit.getNumTroops();
                // attack units engage conflict and reduce number
                int defendingCas = this.defendingTurn();
                if (this.attackUnit.getNumTroops() - defendingCas < 0) {
                    this.attackUnit.setNumTroops(0);
                } else { 
                    this.attackUnit.setNumTroops(attackBefore - defendingCas);
                }
                // add to total engagement
                this.battle.addNumEngagement();
            }

            // attack unit is defeated fully
            if (this.attackUnit.getNumTroops() == 0 && this.defendUnit.getNumTroops() > 0) {
                // add defending unit back to arytm
                this.battle.addDefendUnit(this.defendUnit);
                return;
            }

            // if attact unit routed sucessfully    
            if (this.attackUnit.getNumTroops() > 0 && this.attackUnitRoute == true) {
                // add unit back to start province
                System.out.println("Unit routed with #troops " + this.attackUnit.getNumTroops());
                System.out.println("Unit routed with mv points " + this.attackUnit.getCurrentMovementPoint());
                battle.getAttackArmyProvince().routedUnit(this.attackUnit);
                return;
            }
        }

        System.out.println("at bottom");


    }

    /**
     * Determines the type of engagement
     * Melee or ranged
     */
    public static String determineType(Unit attackUnit, Unit defendUnit) {
        ArrayList<String> attackingType = attackUnit.getType();
        ArrayList<String> defendingType = defendUnit.getType();
        int attackRangeCheck = -1;
        int defendRangeCheck = -1;
        
        if (attackingType.contains("ranged")) {
            attackRangeCheck++;
        }
        if (defendingType.contains("ranged")) {
            defendRangeCheck++;
        }
        if (attackRangeCheck == -1 && defendRangeCheck == -1) {
            return  "melee";
        } else if (attackRangeCheck == 0 && defendRangeCheck == 0) {
            return "ranged";
        } else { // we need to determine the chance
            return differentTypes(attackRangeCheck, defendRangeCheck, attackUnit, defendUnit);
        }

    }


    public static String differentTypes(int attackCheck, int defendCheck, Unit attackUnit, Unit defendUnit) {
        int meleePercentage = 50;
        int attackSpeed = attackUnit.getSpeed();
        int defendSpeed = defendUnit.getSpeed();


        if (attackCheck == 0) { // attack is ranged
            int diff = defendSpeed - attackSpeed;
            diff *= 10;
            meleePercentage += diff;
            if (meleePercentage > 95) {
                meleePercentage = 95;
            }
        } else { // defend is ranged
            int diff = attackSpeed - defendSpeed;
            diff *= 10;
            meleePercentage += diff;
            if (meleePercentage > 95) {
                meleePercentage = 95;
            }
        }

        int rangedPercentage = 100 - meleePercentage;

        ArrayList<String> determine = new ArrayList<String>();
        for (int i = 0; i < meleePercentage; i++) {
            determine.add("melee");
        }
        for (int i = 0; i < rangedPercentage; i++) {
            determine.add("ranged");
        }

        Random rand = new Random(2);
        int randInt = rand.nextInt(100);
        return determine.get(randInt);
    }



     /**
     * Determines the type of engagement
     * Melee or ranged
     */
    public void determineType() {
        ArrayList<String> attackingType = this.attackUnit.getType();
        ArrayList<String> defendingType = this.defendUnit.getType();
        int attackRangeCheck = -1;
        int defendRangeCheck = -1;
        
        if (attackingType.contains("ranged")) {
            attackRangeCheck++;
        }
        if (defendingType.contains("ranged")) {
            defendRangeCheck++;
        }

        if (attackRangeCheck == -1 && defendRangeCheck == -1) {
            engagementType = "melee";
        } else if (attackRangeCheck == 0 && defendRangeCheck == 0) {
            engagementType = "ranged";
        } else { // we need to determine the chance
            engagementType = differentTypes(attackRangeCheck, defendRangeCheck);
        }
    }

    public String differentTypes(int attackCheck, int defendCheck) {
        int meleePercentage = 50;
        int attackSpeed = attackUnit.getSpeed();
        int defendSpeed = defendUnit.getSpeed();
        if (attackCheck == 0) { // attack is ranged
            int diff = defendSpeed - attackSpeed;
            diff *= 10;
            meleePercentage += diff;
            if (meleePercentage > 95) {
                meleePercentage = 95;
            }
        } else { // defend is ranged
            int diff = attackSpeed - defendSpeed;
            diff *= 10;
            meleePercentage += diff;
            if (meleePercentage > 95) {
                meleePercentage = 95;
            }
        }

        int rangedPercentage = 100 - meleePercentage;

        ArrayList<String> determine = new ArrayList<String>();
        for (int i = 0; i < meleePercentage; i++) {
            determine.add("melee");
        }
        for (int i = 0; i < rangedPercentage; i++) {
            determine.add("ranged");
        }

        Random rand = new Random(2);
        int randInt = rand.nextInt(100);
        return determine.get(randInt);
    }


    /**
     * Attack until one 
     * @return
     */
    private int attackingTurn() {
        Random rand = new Random(2);
        double n = rand.nextGaussian();
        int enemySize = this.defendUnit.getNumTroops();
        int attackDmg = this.attackUnit.getAttack();
        int enemyShield = this.defendUnit.getShieldDefense();
        int enemyArmour = this.defendUnit.getArmour();
        int enemyDefenseSkill = this.defendUnit.getDefenseSkill();
        int casualties = 0;

        // if attack unit breaks, it can no longer cause casualties
        if (this.attackUnitBreak == true) {
            return 0;
        }

        if (this.engagementType.equals("ranged")) {
            // will need to add special attacks
            if (this.defendUnit.getType().contains("melee")) { // does no damage
                return 0;
            } else {
                int defence = enemyArmour + enemyShield;
                if (defence > 10 || defence < 0) {
                    defence = 10;
                }
                double casualty = (enemySize * 0.1) * (attackDmg / defence * (n + 1));
                casualties = (int) Math.round(casualty);
                if (casualties < 0) {
                    casualties = 0;
                } else if (casualties > enemySize) {
                    casualties = enemySize;
                }
            }

        } else { // melee attack
            if (attackUnit.getUnitName().equals("melee-cavalry") || attackUnit.getUnitName().equals("chariots")
                    || attackUnit.getUnitName().equals("elephants")) { // charge melee attack
                // charge for now is half of attack
                int charge = attackDmg / 2;
                casualties = charge + attackDmg;

            } else { // regular melee attack
                double casualty = (enemySize * 0.1) * (attackDmg / (enemyArmour + enemyShield + enemyDefenseSkill) * (n + 1));

                //System.out.println("Casualty =" + casualty);

                casualties = (int) Math.round(casualty);
                if (casualties < 0) {
                    casualties = 0;
                } else if (casualties > enemySize) {
                    casualties = enemySize;
                }
                //System.out.println("Casualties =" + casualties);
            }
        }
        return casualties;
    }

    private int defendingTurn() {
        Random rand = new Random(2);
        double n = rand.nextGaussian();
        int enemySize = this.attackUnit.getNumTroops();
        int attackDmg = this.defendUnit.getAttack();
        int enemyShield = this.attackUnit.getShieldDefense();
        int enemyArmour = this.attackUnit.getArmour();
        int enemyDefenseSkill = this.attackUnit.getDefenseSkill();
        int casualties;

        if (this.defendUnitBreak == true) {
            return 0;
        }

        if (this.engagementType.equals("ranged")) {
            // will need to add special attacks
            if (this.attackUnit.getType().contains("melee")) { // does no damage
                return 0;
            } else {
                int defence = enemyArmour + enemyShield;

                if (defence > 10 || defence < 0) {
                    defence = 10;
                }
                double casualty = (enemySize * 0.1) * (attackDmg / defence * (n + 1));
                casualties = (int) Math.round(casualty);
                if (casualties < 0) {
                    casualties = 0;
                } else if (casualties > enemySize) {
                    casualties = enemySize;
                }

            }

        } else { // melee attack
            if (this.defendUnit.getUnitName().equals("melee-cavalry") || this.defendUnit.getUnitName().equals("chariots")
                    || this.defendUnit.getUnitName().equals("elephants")) { // charge melee attack
                // charge for now is half of attack
                int charge = attackDmg / 2;
                casualties = charge + attackDmg;
                this.attackUnit.lostUnits(casualties);
            } else { // regular melee attack
                double casualty = (enemySize * 0.1)
                        * (attackDmg / (enemyArmour + enemyShield + enemyDefenseSkill) * (n + 1));
                casualties = (int) Math.round(casualty);
                if (casualties < 0) {
                    casualties = 0;
                } else if (casualties > enemySize) {
                    casualties = enemySize;
                }
            }
        }
        return casualties;
    }


    private void breaking(int attackCasualties, int defendCasualties) {

        // calculate attacking unit's chance of breaking
        int attackBase = 100 - (this.attackUnit.getMorale() * 10);

        int attackCalc = (attackCasualties + this.attackUnit.getNumTroops());
        int defendCalc = (defendCasualties + this.defendUnit.getNumTroops());

        if (attackCalc == 0)
            attackCalc = 1;
        if (defendCalc == 0)
            defendCalc = 1;

        int check = defendCasualties / defendCalc;
        int check2 = attackCasualties / attackCalc;

        if (check == 0)
            check = 1;
        if (check2 == 0)
            check2 = 1;

        int attackChance = check2 / check * 10;

        attackChance += attackBase;

        if (attackChance < 5) {
            attackChance = 5;
        } else if (attackChance > 100) {
            attackChance = 100;
        }

        ArrayList<Integer> attackDetermine = new ArrayList<Integer>();
        for (int i = 0; i < attackChance; i++) {
            attackDetermine.add(1);
        }
        for (int i = 0; i < (100 - attackChance); i++) {
            attackDetermine.add(0);
        }

        Random rand = new Random(2);
        int randInt = rand.nextInt(100);

        if (attackDetermine.get(randInt) == 1) { // attack breaks
            this.attackUnitBreak = true;
        }

        // calculate defending team's chance of breaking

        if (this.attackUnitBreak != true) {

            int defendBase = 100 - (this.defendUnit.getMorale() * 10);
            int defendChance = check / check2 * 10;
            defendChance += defendBase;

            if (defendChance < 5) {
                defendChance = 5;
            } else if (defendChance > 100) {
                defendChance = 100;
            }

            ArrayList<Integer> defendDetermine = new ArrayList<Integer>();

            for (int i = 0; i < defendChance; i++) {
                defendDetermine.add(1);
            }

            for (int i = 0; i < (100 - defendChance); i++) {
                defendDetermine.add(0);
            }

            if (defendDetermine.get(randInt) == 1) {
                this.defendUnitBreak = true;
            }
        }

    }

    private boolean route() {
        int chance = 50 + (10 * (this.attackUnit.getSpeed() - this.defendUnit.getSpeed()));
        ArrayList<Integer> determine = new ArrayList<Integer>();
        for (int i = 0; i < chance; i++) {
            determine.add(1);
        }
        for (int i = 0; i < (100 - chance); i++) {
            determine.add(0);
        }
        Random rand = new Random(2);
        int randInt = rand.nextInt(100);
        if (determine.get(randInt) == 1) { // route is successful
            this.attackUnitRoute = true;
            return true;
        }
        return false;
    }

}
