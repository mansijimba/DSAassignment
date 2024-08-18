package com.example;

import java.util.*;
class ClassroomSchedule {
        public static int mostUsedClassroom(int n, int[][] classes) {
            Arrays.sort(classes, (a, b) -> {
                if (a[0] != b[0]) return a[0] - b[0];
                return b[1] - a[1];
            });
            PriorityQueue<int[]> roomQueue = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            int[] roomUsage = new int[n];

            for (int i = 0; i < n; i++) {
                roomQueue.offer(new int[]{0, i});
            }

            for (int[] cls : classes) {
                int start = cls[0], end = cls[1];
                while (!roomQueue.isEmpty() && roomQueue.peek()[0] <= start) {
                    roomQueue.poll();
                }

                if (roomQueue.isEmpty()) {
                    int[] earliestRoom = roomQueue.poll();
                    start = earliestRoom[0];
                    end = start + (end - start);
                }
                roomQueue.offer(new int[]{end, roomQueue.size()});
                roomUsage[roomQueue.size() - 1]++;
            }

            int maxUsage = 0, mostUsedRoom = 0;
            for (int i = 0; i < n; i++) {
                if (roomUsage[i] > maxUsage || (roomUsage[i] == maxUsage && i < mostUsedRoom)) {
                    maxUsage = roomUsage[i];
                    mostUsedRoom = i;
                }
            }


            return mostUsedRoom;
        }

        public static void main(String[] args) {
            int n = 2;
            int[][] classes = {
                    {0, 10},
                    {1, 5},
                    {2, 7},
                    {3, 4}
            };
            System.out.println(mostUsedClassroom(n, classes));
        }
}
