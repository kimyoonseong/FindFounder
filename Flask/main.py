from flask import Flask
from Findfounder import create_app

app = create_app()



if __name__ == "__main__":
    # Only for debugging while developing
    app.run(host='0.0.0.0', debug=True)