CREATE DATABASE IF NOT EXISTS vitalis CHARACTER SET utf8mb4;
USE vitalis;

CREATE TABLE usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(120) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  idade INT NOT NULL,
  senha VARCHAR(120) NOT NULL,
  tipo_usuario ENUM('JOGADOR','ADMINISTRADOR') NOT NULL DEFAULT 'JOGADOR'
);

CREATE TABLE cenarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(150) NOT NULL,
  narrativa TEXT NOT NULL,
  ordem_fase INT NOT NULL UNIQUE,
  imagem VARCHAR(255)
);

CREATE TABLE opcoes_escolha (
  id INT AUTO_INCREMENT PRIMARY KEY,
  cenario_id INT NOT NULL,
  texto VARCHAR(400) NOT NULL,
  e_correta BOOLEAN NOT NULL DEFAULT FALSE,
  feedback_didatico TEXT NOT NULL,
  narrativa_sucesso TEXT,
  FOREIGN KEY (cenario_id) REFERENCES cenarios(id) ON DELETE CASCADE
);

CREATE TABLE progressos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  jogador_id INT NOT NULL,
  cenario_id INT NOT NULL,
  pontuacao INT NOT NULL DEFAULT 0,
  status_partida ENUM('EM_ANDAMENTO','GAME_OVER','CONCLUIDO') NOT NULL DEFAULT 'EM_ANDAMENTO',
  data_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (jogador_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  FOREIGN KEY (cenario_id) REFERENCES cenarios(id) ON DELETE CASCADE
);

CREATE TABLE certificados (
  id INT AUTO_INCREMENT PRIMARY KEY,
  jogador_id INT NOT NULL,
  codigo_verificador VARCHAR(40) NOT NULL UNIQUE,
  data_conclusao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  pontuacao_final INT NOT NULL DEFAULT 0,
  FOREIGN KEY (jogador_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE biblioteca (
  id INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(150) NOT NULL,
  conteudo TEXT NOT NULL,
  categoria VARCHAR(80) NOT NULL
);

INSERT INTO usuarios (nome,email,idade,senha,tipo_usuario) VALUES
('Administrador','admin@vitalis.com',30,'admin123','ADMINISTRADOR'),
('Jogador Demo','demo@vitalis.com',20,'demo123','JOGADOR');

INSERT INTO cenarios (titulo,narrativa,ordem_fase,imagem) VALUES
('O Almoço de Domingo (Engasgo)',
 'Você está almoçando com sua família quando seu tio leva as mãos ao pescoço. Ele arregala os olhos, não consegue falar, tossir ou respirar, e seu rosto começa a ficar vermelho. O pânico toma conta da sala.',
 1,'engasgo.png'),

('A Queda no Parque (Parada Cardiorrespiratória)',
 'Você caminha no parque quando um homem cai abruptamente à sua frente. Você o chama em voz alta e toca em seus ombros, mas ele não responde. O peito dele não se move.',
 2,'pcr.png'),

('Dor no Peito em Casa (Infarto)',
 'Seu pai sente uma dor forte no peito, irradiando para o braço esquerdo, com suor frio e falta de ar. Ele está pálido e assustado.',
 3,'infarto.png');

INSERT INTO opcoes_escolha (cenario_id,texto,e_correta,feedback_didatico,narrativa_sucesso) VALUES
(1,'Oferecer um copo de água e dar tapas fortes nas costas dele enquanto ele está sentado.',FALSE,
 'Oferecer líquidos a uma pessoa com obstrução total das vias aéreas pode empurrar o alimento ainda mais fundo ou ir para o pulmão, piorando a situação. Tapas nas costas de uma pessoa sentada também podem agravar o bloqueio. O protocolo correto para engasgo total é a Manobra de Heimlich.',NULL),
(1,'Posicionar-se imediatamente atrás dele, abraçá-lo e realizar a Manobra de Heimlich (compressões firmes na "boca do estômago").',TRUE,
 'Correto! A Manobra de Heimlich é o protocolo indicado para obstrução total das vias aéreas em vítima consciente.',
 'Você aplica três compressões firmes. Na terceira, o pedaço de carne é expulso da garganta do seu tio. Ele puxa o ar profundamente e volta a respirar. A família respira aliviada.'),
(1,'Deitá-lo no chão e iniciar compressões torácicas, pois ele está sem respirar.',FALSE,
 'A vítima está consciente e de pé: a compressão torácica é indicada para parada cardiorrespiratória, não para engasgo. Neste caso, o protocolo é a Manobra de Heimlich, aplicada com a vítima em pé.',NULL);

INSERT INTO opcoes_escolha (cenario_id,texto,e_correta,feedback_didatico,narrativa_sucesso) VALUES
(2,'Ligar para o SAMU (192) no viva-voz e iniciar imediatamente compressões rápidas e fortes no centro do peito dele.',TRUE,
 'Correto! Acionar a emergência e iniciar a massagem cardíaca imediatamente é o protocolo para parada cardiorrespiratória.',
 'O atendente do SAMU orienta o ritmo das compressões enquanto a ambulância está a caminho. Seu atendimento primário rápido mantém o sangue circulando para o cérebro, dando uma chance real de sobrevivência à vítima até a chegada do resgate.'),
(2,'Colocar uma jaqueta enrolada debaixo da cabeça dele para dar conforto e esperar ele acordar sozinho.',FALSE,
 'Em casos de parada cardiorrespiratória, cada minuto sem massagem cardíaca reduz drasticamente as chances de sobrevivência e causa danos cerebrais irreversíveis. Nunca espere a pessoa "acordar". Ligue para a emergência e inicie as compressões torácicas imediatamente.',NULL),
(2,'Dar água e sacudir os ombros dele com força para tentar acordá-lo.',FALSE,
 'Sacudir e oferecer líquidos a uma vítima inconsciente é perigoso: pode causar aspiração e agravar lesões. O correto é acionar o SAMU e iniciar as compressões torácicas imediatamente.',NULL);

INSERT INTO opcoes_escolha (cenario_id,texto,e_correta,feedback_didatico,narrativa_sucesso) VALUES
(3,'Acionar o SAMU imediatamente, mantê-lo em repouso e monitorar a respiração até a chegada do socorro.',TRUE,
 'Correto! No infarto, o tempo é músculo: acionar a emergência rápido e manter repouso é o protocolo.',
 'Você liga para o SAMU, descreve os sintomas e mantém seu pai sentado e calmo. A ambulância chega rápido e ele recebe atendimento a tempo.'),
(3,'Dar um comprimido de analgésico comum e esperar a dor passar em casa.',FALSE,
 'Dor no peito com irradiação para o braço e suor frio é sinal de infarto. Esperar em casa aumenta o risco de morte. O correto é acionar o SAMU imediatamente e manter a vítima em repouso.',NULL),
(3,'Levar a pessoa ao hospital dirigindo o próprio carro, mesmo com ela piorando.',FALSE,
 'Transportar por conta própria uma vítima de infarto é arriscado: ela pode parar de respirar no caminho, sem suporte. O correto é acionar o SAMU, que traz desfibrilador e equipe treinada.',NULL);

INSERT INTO biblioteca (titulo,conteudo,categoria) VALUES
('Manobra de Heimlich','Posicione-se atrás da vítima consciente, feche o punho e pressione firme e rapidamente acima do umbigo, em movimentos para dentro e para cima, até a desobstrução.','Engasgo'),
('RCP','Acione o SAMU, coloque a vítima em superfície rígida e comprima o centro do peito 100 a 120 vezes por minuto, 5 a 6 cm de profundidade.','Parada Cardiorrespiratória'),
('Infarto','Dor no peito irradiando, suor frio e falta de ar. Acione o SAMU, mantenha repouso e monitore a respiração.','Infarto'),
('Acionamento do SAMU','Ligue 192, informe local, ocorrência e número de vítimas. Mantenha em viva-voz para receber orientações.','Protocolo Geral');