FROM tiangolo/uwsgi-nginx:python3.10

# Install requirements
COPY requirements.txt /tmp/requirements.txt
RUN pip install --no-cache-dir -r /tmp/requirements.txt

# URL under which static (not modified by Python) files will be requested
# They will be served by Nginx directly, without being handled by uWSGI
ENV STATIC_URL /static
# Absolute path in where the static files wil be
ENV STATIC_PATH /app/static

# If STATIC_INDEX is 1, serve / with /static/index.html directly (or the static URL configured)
# ENV STATIC_INDEX 1
ENV STATIC_INDEX 0

# Add demo app
COPY . /app 
WORKDIR /app/Findfounder


# Expose port 5000 (or the port your Flask app runs on)
EXPOSE 5000

# Set the Flask app environment variable
ENV FLASK_APP main:app

CMD ["python3", "-m", "flask", "run", "--host=0.0.0.0"]