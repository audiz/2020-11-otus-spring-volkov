package ru.otus.work3.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service("inputService")
public class InputServiceImpl implements InputService {
    @Override
    public String getInput(String info) {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println(info);
            return br.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
