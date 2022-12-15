package main.java;

import static main.java.Utils.generateStat;
import static main.java.Utils.randomNumberGenerator;

public class Stats {
    public int Attack;
    public int Defense;
    public int BodyBalance;
    public int Stamina;
    public int TopSpeed;
    public int Acceleration;
    public int Response;
    public int Agility;
    public int DribbleAccuracy;
    public int DribbleSpeed;
    public int ShortPassAccuracy;
    public int ShortPassSpeed;
    public int LongPassAccuracy;
    public int LongPassSpeed;
    public int ShotAccuracy;
    public int ShotPower;
    public int ShotTechnique;
    public int FreeKickAccuracy;
    public int Swerve;
    public int Header;
    public int Jump;
    public int Technique;
    public int Mentality;
    public int KeeperSkills;
    public int Teamwork;
    public int Condition;
    public int WeakFootAccuracy;
    public int WeakFootFrequency;


    public Stats() {
        this.Attack = randomNumberGenerator(1, 99);
        this.Defense = randomNumberGenerator(1, 99);
        this.BodyBalance = generateStat();
        this.Stamina = generateStat();
        this.TopSpeed = generateStat();
        this.Acceleration = generateStat();
        this.Response = generateStat();
        this.Agility = generateStat();
        this.DribbleAccuracy = generateStat();
        this.DribbleSpeed = generateStat();
        this.ShortPassAccuracy = generateStat();
        this.ShortPassSpeed = generateStat();
        this.LongPassAccuracy = generateStat();
        this.LongPassSpeed = generateStat();
        this.ShotAccuracy = generateStat();
        this.ShotPower = generateStat();
        this.ShotTechnique = generateStat();
        this.FreeKickAccuracy = generateStat();
        this.Swerve = generateStat();
        this.Header = generateStat();
        this.Jump = generateStat();
        this.Technique = generateStat();
        this.Mentality = generateStat();
        this.KeeperSkills = generateStat();
        this.Teamwork = generateStat();
        this.Condition = generateStat();
        this.WeakFootAccuracy = randomNumberGenerator(1, 8);
        this.WeakFootFrequency = randomNumberGenerator(1, 8);
    }

    //toString using string.format
    @Override
    public String toString() {
        return String.format("""
                        Attack: %d
                        Defense: %d
                        Body Balance: %d
                        Stamina: %d
                        Top Speed: %d
                        Acceleration: %d
                        Response: %d
                        Agility: %d
                        Dribble Accuracy: %d
                        Dribble Speed: %d
                        Short Pass Accuracy: %d
                        Short Pass Speed: %d
                        Long Pass Accuracy: %d
                        Long Pass Speed: %d
                        Shot Accuracy: %d
                        Shot Power: %d
                        Shot Technique: %d
                        Free Kick Accuracy: %d
                        Swerve: %d
                        Header: %d
                        Jump: %d
                        Technique: %d
                        Mentality: %d
                        Keeper Skills: %d
                        Teamwork: %d
                        Condition: %d
                        Weak Foot Accuracy: %d
                        Weak Foot Frequency: %d
                        """,
                this.Attack, this.Defense, this.BodyBalance, this.Stamina, this.TopSpeed, this.Acceleration, this.Response, this.Agility, this.DribbleAccuracy, this.DribbleSpeed, this.ShortPassAccuracy, this.ShortPassSpeed, this.LongPassAccuracy, this.LongPassSpeed, this.ShotAccuracy, this.ShotPower, this.ShotTechnique, this.FreeKickAccuracy, this.Swerve, this.Header, this.Jump, this.Technique, this.Mentality, this.KeeperSkills, this.Teamwork, this.Condition, this.WeakFootAccuracy, this.WeakFootFrequency);
    }


}
