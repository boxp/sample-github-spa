module.exports = {
  "globDirectory": "resources/public/prod",
  "globPatterns": [
    "js/compiled/*.js"
  ],
  "modifyUrlPrefix": {
      "js/compiled": "/static/js/compiled",
  },
  "swDest": "resources/public/prod/sw.js",
  "runtimeCaching": [
      {
          "urlPattern": /\//,
          "handler": "networkFirst",
          "options": {
              "cacheableResponse": {
                  "statuses": [0, 200],
                  "headers": {
                      "Content-type": "text/html; charset=utf-8",
                  },
              },
          },
      },
      {
          "urlPattern": /^https\:\/\/use\.fontawesome\.com\/releases\//,
          "handler": "cacheFirst",
          "options": {
              "cacheableResponse": {
                  "statuses": [0, 200],
              },
          },
      },
      {
          "urlPattern": /^https\:\/\/api\.github\.com\//,
          "handler": "networkFirst",
          "options": {
              "cacheableResponse": {
                  "statuses": [0, 200],
              },
          },
      },
      {
          "urlPattern": /^https\:\/\/avatars0\.githubusercontent\.com\//,
          "handler": "cacheFirst",
          "options": {
              "cacheableResponse": {
                  "statuses": [0, 200],
              },
          },
      },
  ],
};
