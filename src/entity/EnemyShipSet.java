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
    // 적 함선의 이동속도
    private int movementSpeed;
    // 적 함선의 X 방향 속도
    private int X_speed = 1;
    // 적 함선의 Y 방향 속도
    private int Y_speed = 1;



    /**
     * 생성자 - 기본 set 초기화 및 스폰 준비
     */
    public EnemyShipSet(GameSettings gameSettings, Ship ship) {
        this.enemies = new HashSet<>();
        this.spawnCooldown = Core.getCooldown(gameSettings.getEnemySpawnInterval());
        this.drawManager = Core.getDrawManager();
        this.random = new Random();
        this.ship = ship;
        this.movementSpeed = gameSettings.getBaseSpeed();
    }

    /**
     * 화면에 적 생성 후 이동하는 것 업데이트
     */
    public void update() {
        // 스폰 쿨타임이 다 돌았으면 생성
        if (this.spawnCooldown.checkFinished()) {
            this.spawnCooldown.reset();
            spawnEnemy();

        }

        int movement_X = 0;
        int movement_Y = 0;

        // 각 적 객체에 대해 업데이트
        for (EnemyShip enemy : enemies) {
            enemy.update();
            // 적이 플레이어를 따라가도록 설정
            movement_X = (ship.getPositionX() > enemy.getPositionX()) ? X_speed : -X_speed;
            movement_Y = (ship.getPositionY() > enemy.getPositionY()) ? Y_speed : -Y_speed;

            enemy.move(movement_X, movement_Y);

        }
    }


    /**
     * 적을 생성해주는 메소드
     */
    private void spawnEnemy() {
        int spawnX, spawnY;
        int minDistance = 100; // 플레이어와의 최소 거리 우선 100으로 설정

        // 플레이어로부터 일정 거리 떨어진 위치에서만 생성되도록 설정
        do {
            spawnX = random.nextInt(screen.getWidth());
            spawnY = random.nextInt(screen.getHeight());
        } while (Math.hypot(spawnX - ship.getPositionX(), spawnY - ship.getPositionY()) < minDistance);

        // 적 생성
        EnemyShip newEnemy = new EnemyShip(spawnX, spawnY, SpriteType.EnemyShipA1);
        // 생성된 적 객체를 Set에 추가
        enemies.add(newEnemy);
    }

    /**
     * 생성된 적들을 draw하는 메소드
     */
    public void draw() {
        for (EnemyShip enemy : enemies) {
            drawManager.drawEntity(enemy, enemy.positionX, enemy.positionY);
        }
    }

    public final void attach(final Screen newscreen) {
        screen = newscreen;
    }

    public Set<EnemyShip> getEnemies() {
        return enemies;
    }

    public void destroy(EnemyShip enemyShip) {
        for (EnemyShip enemy : enemies) {
            if (enemy.equals(enemyShip)) {
                enemy.destroy();
            }
        }
    }
}
