package org.JE.JE2.Utility.Settings;

import org.JE.JE2.Resources.ResourceLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SettingManager {
    private final ArrayList<SettingCategory> categories = new ArrayList<>();

    public SettingManager() {
    }

    public SettingManager(SettingCategory... categories) {
        this.categories.addAll(List.of(categories));
    }

    public SettingCategory getCategory(String name) {
        for(SettingCategory category : categories) {
            if(category.getCategoryName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    public SettingCategory getCategory(int index) {
        return categories.get(index);
    }

    public void addCategory(SettingCategory category) {
        categories.add(category);
    }

    public void saveToFile(String path){
        try {
            FileWriter fileWriter = new FileWriter(path);
            for (SettingCategory category :
                    categories) {
                fileWriter.write("Category:" + category.getCategoryName() + "\n");

                for (String line :
                        writeCategory(category)) {
                    fileWriter.write(line + "\n");
                }
            }
            fileWriter.close();
        }catch (Exception ignore){
            System.out.println("error saving settings");
        }
    }

    private String[] writeCategory(SettingCategory category){
        ArrayList<String> lines = new ArrayList<>();
        for (Setting<?> setting :
                category.settings) {
            lines.add(setting.getName());
            try {
                lines.add(serialize(setting.getValue()));
            }catch (Exception e){
                System.out.println("error serializing: " + setting.getName());
            }
        }
        return lines.toArray(new String[0]);
    }

    private String serialize(Object object) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private static Object deserialize(String input){
        try {
            byte[] bytes = Base64.getDecoder().decode(input);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object o = ois.readObject();
            ois.close();
            return o;
        }
        catch (Exception ignore){
            System.out.println("error deserializing: " + input);
            return new Object();
        }
    }

    public void tryLoadFromFile(String path){
        File file = new File(path);
        if(!file.exists()){
            return;
        }
        String[] lines = ResourceLoader.readTextFile(path);
        SettingCategory activeCat = null;
        for (int i = 0; i < lines.length; i++) {
            if(lines[i].startsWith("Category:")){
                activeCat = getCategory(lines[i].trim().replace("Category:",""));
            }
            else if(activeCat!=null) {
                String settingName = lines[i].trim();
                Object settingValue = deserialize(lines[i+1].trim());
                Setting<?> setting = activeCat.getSetting(settingName);
                setting.setValueObject(settingValue);
                //System.out.println("Set setting: " + settingName + " to " + setting.getValue());
                i++;
            }
        }
    }
}
