/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80026 (8.0.26)
 Source Host           : localhost:3306
 Source Schema         : medical

 Target Server Type    : MySQL
 Target Server Version : 80026 (8.0.26)
 File Encoding         : 65001

 Date: 25/04/2026 12:54:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for appointments
-- ----------------------------
DROP TABLE IF EXISTS `appointments`;
CREATE TABLE `appointments`  (
  `appointment_date_time` datetime(6) NOT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `doctor_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKmujeo4tymoo98cmf7uj3vsv76`(`doctor_id` ASC) USING BTREE,
  INDEX `FK886ced1atxgvnf1o3oxtj5m4s`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FK886ced1atxgvnf1o3oxtj5m4s` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKmujeo4tymoo98cmf7uj3vsv76` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of appointments
-- ----------------------------
INSERT INTO `appointments` VALUES ('2026-03-10 11:44:00.000000', '2026-03-10 11:44:09.213822', 3, 4, 2, '', 'COMPLETED');
INSERT INTO `appointments` VALUES ('2026-04-24 12:08:00.000000', '2026-04-24 12:09:01.485847', 5, 8, 10, '', 'PENDING');
INSERT INTO `appointments` VALUES ('2026-04-25 02:40:00.000000', '2026-04-25 02:40:30.638112', 5, 9, 11, '', 'PENDING');
INSERT INTO `appointments` VALUES ('2026-04-25 02:59:00.000000', '2026-04-25 02:59:09.035844', 3, 10, 12, '', 'CONFIRMED');

-- ----------------------------
-- Table structure for departments
-- ----------------------------
DROP TABLE IF EXISTS `departments`;
CREATE TABLE `departments`  (
  `created_at` datetime(6) NULL DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of departments
-- ----------------------------
INSERT INTO `departments` VALUES ('2026-03-07 13:12:59.568438', 1, '神经科', NULL);
INSERT INTO `departments` VALUES ('2026-03-07 13:47:29.665648', 2, '眼科', NULL);
INSERT INTO `departments` VALUES ('2026-03-07 13:47:40.846496', 3, '牙科', NULL);
INSERT INTO `departments` VALUES ('2026-04-23 03:16:58.325956', 4, '外科', NULL);

-- ----------------------------
-- Table structure for doctors
-- ----------------------------
DROP TABLE IF EXISTS `doctors`;
CREATE TABLE `doctors`  (
  `created_at` datetime(6) NULL DEFAULT NULL,
  `department_id` bigint NULL DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `specialization` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKl2mro81neln9topymd898urh1`(`department_id` ASC) USING BTREE,
  CONSTRAINT `FKl2mro81neln9topymd898urh1` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doctors
-- ----------------------------
INSERT INTO `doctors` VALUES ('2026-03-07 13:46:51.535599', 1, 3, '1111111', '杨一', '脑梗', 3);
INSERT INTO `doctors` VALUES ('2026-03-07 13:47:12.692560', 4, 4, '2222222', '杨二', '皮肤病', 4);
INSERT INTO `doctors` VALUES ('2026-03-07 13:49:04.256486', 2, 5, '3333333', '杨三', '干眼症', 5);
INSERT INTO `doctors` VALUES ('2026-03-07 13:49:50.630637', 3, 6, '4444444', '杨四', '蛀牙', 6);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `created_at` datetime(6) NULL DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UKr43af9ap4edm43mmtq01oddj6`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('2026-02-20 19:39:02.000000', 1, '000', 'ADMIN', '管理员', 'admin', 'admin@test.com', '123456');
INSERT INTO `users` VALUES ('2026-02-20 19:39:02.000000', 2, '111', 'USER', '张三', 'user', 'user@test.com', '123456');
INSERT INTO `users` VALUES ('2026-02-20 19:39:02.000000', 3, '1111111', 'DOCTOR', '杨一', 'doctor1', 'doctor@test.com', '123456');
INSERT INTO `users` VALUES ('2026-03-07 13:36:41.890331', 4, NULL, 'DOCTOR', '杨二', 'doctor2', NULL, '123456');
INSERT INTO `users` VALUES ('2026-03-07 13:48:33.208872', 5, NULL, 'DOCTOR', '杨三', 'doctor3', NULL, '123456');
INSERT INTO `users` VALUES ('2026-03-07 13:49:26.073320', 6, NULL, 'DOCTOR', '杨四', 'doctor4', NULL, '123456');
INSERT INTO `users` VALUES ('2026-04-23 03:39:42.947583', 9, NULL, 'USER', '远1', 'user1', NULL, '123456');
INSERT INTO `users` VALUES ('2026-04-24 12:08:01.273706', 10, NULL, 'USER', '远2', 'user2', NULL, '123456');
INSERT INTO `users` VALUES ('2026-04-25 02:39:45.692192', 11, NULL, 'USER', '远3', 'user3', NULL, '123456');
INSERT INTO `users` VALUES ('2026-04-25 02:58:31.611220', 12, NULL, 'USER', '远4', 'user4', NULL, '123456');

SET FOREIGN_KEY_CHECKS = 1;
