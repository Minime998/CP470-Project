package com.example.camlingo.gpt_api;

public class gpt_api {

    public static class ImgUpload {
        private final String image;

        public ImgUpload(String image){
            this.image = image;
        }

        public String getImg(){
            return image;
        }

    }

    public static class PredictResponse {
        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

}






