-- 创建player表：用于存储小猫娘的信息
CREATE TABLE player (
    id INTEGER PRIMARY KEY,
    money INTEGER NOT NULL
);

-- 创建rinkko表：用于存储所有凛喵喵的信息
CREATE TABLE rinkko (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    hunger INTEGER,
    thirst INTEGER,
    mood INTEGER,
    health INTEGER,
    affection INTEGER,
    player_id INTEGER,
    FOREIGN KEY (player_id) REFERENCES player(id)
);