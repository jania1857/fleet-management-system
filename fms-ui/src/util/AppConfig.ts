class AppConfig {
    private constructor() {
    }

    // true - local backend
    // false - aws backend
    static readonly USE_AWS = false;

    static readonly AWS_BASE_PATH = "http://ec2-51-20-91-57.eu-north-1.compute.amazonaws.com:8080/";
    static readonly LOCAL_BASE_PATH = "http://localhost:8080/";

    static readonly getBasePath = () => {
        return AppConfig.USE_AWS ? AppConfig.AWS_BASE_PATH : AppConfig.LOCAL_BASE_PATH
    }
}

export default AppConfig;