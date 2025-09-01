import processing.core.PApplet;
import processing.core.PImage;
public class kirby extends PApplet {
    PImage character;
    PImage bg;
    PImage rock;
    public boolean moveLeft = false;
    public boolean moveRight = false;

    public int bgX = 0;
    public int bgY = 0;
    public int maxWidth = 500;
    public int maxHeight = 700;
    public float bgSpeed = 2;

    public int charWidth = 100;
    public int charHeight = 80;
    public float charX = maxWidth/2;
    public float charY = maxHeight - charHeight - 20;
    float moveSpeed = 5;

    public float rockSpeed = 4;
    public float rockWidth = 100;
    public float rockHeight = 115;
    public int rockX = (int)(Math.random() * ( (maxWidth - rockWidth/2) - -rockWidth/2) + -rockWidth/2);
    public int rockY = -250;

    public int timer;
    public int startTime;
    public int score = 0;
    public int highScore = 0;
    enum gameState{
        GAMEOVER, RUNNING
    }
    static gameState currentState;
    PImage[] kirbo = new PImage[5];
    int animFrame = 1;
    float animSpeed = 10;
    int count = 0;

    PImage[] rockAnim = new PImage[8];
    int rockFrame = 1;

    public static void main(String[] args){
        PApplet.main("kirby");
    }

    public void setup(){
        for(int i = 0; i < 5; i++){
            kirbo[i] = loadImage("images/kirbo" + (i+1) + ".png");
            kirbo[i].resize(charWidth, charHeight);
        }

        for(int i = 0; i < 8; i++){
            rockAnim[i] = loadImage("images/rock" + (i+1) + ".png");
            rockAnim[i].resize((int)rockWidth, (int)rockHeight);
        }
        bg = loadImage("images/vertBgR.png");
        startTime = millis();
        currentState = gameState.RUNNING;

    }

    public void settings(){
        size(maxWidth, maxHeight);
    }

    public void draw(){
        switch(currentState) {
            case RUNNING:
                move();
                drawBackground();
                if(frameCount % animSpeed == 0){
                    animFrame++;
                    animFrame = animFrame % 5;
                }

                if(frameCount % 3 == 0){
                    rockFrame++;
                    rockFrame = rockFrame % 8;
                }
                drawRock();
                drawChar();
                timer = ( (millis() - startTime) / 1000);
                drawScore();
                break;
            case GAMEOVER:
                drawGameOver();
                break;
        }
    }

    public void drawScore(){
        fill(0, 0, 0);
        textAlign(CORNER);
        textSize(12);
        text("score: " + timer, width - 70, 30);
    }

    public void drawChar(){
        image(kirbo[animFrame], charX, charY);
    }

    public void drawRock(){
        //rock size = 200 x 230
        image(rockAnim[rockFrame], rockX, rockY);
        rockY += (int)rockSpeed;

        //reset rock
        if(rockY + rockHeight > maxHeight + rockHeight){
            rockY = -150;
            rockX = (int)(Math.random() * ( (maxWidth - rockWidth/2) - -rockWidth/2) + -rockWidth/2);
            if(rockWidth <= 165){
                rockWidth += 5;
                rockHeight += 5.75;
                for(int i = 0; i < rockAnim.length; i++){
                    rockAnim[i].resize((int)rockWidth, (int)rockHeight);
                }
            }
            if(rockSpeed <= 17){
                rockSpeed += 0.75;
            }
            if(bgSpeed <= 18){
                bgSpeed += 0.5;
            }

            if(animSpeed >= 5 && count % 3 == 0){
                animSpeed -= 1;
            }
            count++;
            if(moveSpeed <= 13){
                moveSpeed += 0.2;
            }
        }

        //collisions
        if( (rockY + rockHeight >= charY + 5 && rockY <= charY + charHeight - 40) && (charX + charWidth >= rockX && charX <= rockX + rockWidth)){
            score = timer;
            if(score > highScore){
                highScore = score;
            }
            currentState = gameState.GAMEOVER;
        }

    }

    public void drawBackground(){
        imageMode(CORNER);
        image(bg, bgX, bgY);
        image(bg, bgX, bgY + bg.height);
        bgY -= bgSpeed;

        if(bgY <= bg.height * -1){
            bgY = 0;
        }
    }

    public void drawGameOver(){
        fill(225, 200, 255);
        rect(maxWidth/3, maxHeight/3, maxWidth/3, maxHeight / 5);
        fill(1, 1, 1);
        textAlign(CENTER);
        textSize(20);
        text("game over", width/2, height/2 - 60);
        text("score: " + score, width/2, height/2 - 40);
        text("high score: " + highScore, width/2, height/2 - 20);
        textSize(15);
        text("click to restart", width/2, height/2);
    }

    public void keyPressed(){
        if(key == 'a'){
            moveLeft = true;
        }

        if(key == 'd'){
            moveRight = true;
        }
    }

    public void keyReleased(){
        if(key == 'a'){
            moveLeft = false;
        }

        if(key == 'd'){
            moveRight = false;
        }
    }

    public void mousePressed(){
        //restart game
        if(currentState == gameState.GAMEOVER){
            rockX = 100;
            rockY = -250;
            bgX = 0;
            startTime = millis();
            rockSpeed = 4;
            bgSpeed = 2;
            moveSpeed = 5;
            rockHeight = 115;
            rockWidth = 100;
            animSpeed = 10;
            for(int i = 0; i < rockAnim.length; i++){
                rockAnim[i].resize((int)rockWidth, (int)rockHeight);
            }
            currentState = gameState.RUNNING;
        }
    }

    public void move(){
        if(moveLeft && charX >= 0){
            charX -= moveSpeed;
        }

        if(moveRight && charX + charWidth <= maxWidth){
            charX += moveSpeed;
        }

    }
}
