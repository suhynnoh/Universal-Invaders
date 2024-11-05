package entity;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import screen.Screen;
import engine.Cooldown;
import engine.Core;
import engine.DrawManager;
import engine.DrawManager.SpriteType;
import engine.GameSettings;

public class EnemyShipSet {

    // 생성된 적들을 관리하기 위한 Set
    private Set<EnemyShip> enemies;
    // 적 생성 쿨타임
    private Cooldown spawnCooldown;
    // DrawManager 인스턴스
    private DrawManager drawManager;
    // 게임 화면 정보
    private Screen screen;
    // 적 랜덤 생성을 위한 랜덤 객체
    private Random random;
    // 아군 함선 참조를 위한 변수
    private Ship ship;

    /**
     * 생성자 - 기본 set 초기화 및 스폰 준비
     */
    public EnemyShipSet(GameSettings gameSettings, Ship ship) {
        this.enemies = new HashSet<>();
        this.spawnCooldown = Core.getCooldown(gameSettings.getEnemySpawnInterval());
        this.drawManager = Core.getDrawManager();
        this.random = new Random();
        this.ship = ship;
    }
}
