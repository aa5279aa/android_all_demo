#include <iostream>
#include <string>

using namespace std;

class DrawCircle { //绘制圆形，抽象类
    public:
        (1) ;//定义参数为 int radius, int x, int y virtual～DrawCircle() { }
    };

    class RedCircle:public DrawCircle { //绘制红色圆形
    public:
    void drawCircle(int radius, int x, int y) {
        cout << "Drawing Circle[red,radius: "
             << radius;
        cout << ",x: " << x << ",y: " << y << "]" << end1;
    }
};

class GreenCircle:public DrawCircle { //绘制绿色圆形
    public:
    void drawCircle(int radius, int x, int y) {
        cout << "Drawing Circle[green,radius: " <<
             radius;
        cout << ",x: " << x << ",y: " << y << "]" <<
             end1;
    }
};

class Shape { //形状，抽象类
protected:
    (2);
public:
    Shape(DrawCircle *drawCircle) {
        this->drawCircle = drawCircle;
    }

    virtual~shape() {}

public:
    virtual void draw() = 0;
};

class Circle:public Shape { //圆形
    private:
    int x, y, radius;
    public:
    Circle(int x, int y, int radius, DrawCircle *drawCircle,(3))
    {
        this->x = x;
        this->y = y;
        this->radius = radius;
    }
    public:

    void draw() {
        drawCircle->(4)
    }

};

int main() {
    //绘制红色圆形
    Shape *redCircle = new Circle(100, 100, 10,(5));
    // 绘制绿色圆形
    Shape *greenCircle = new Circle(100, 100, 10,(6));
    redCircle->draw();
    greenCircle->draw();
    return 0;
}