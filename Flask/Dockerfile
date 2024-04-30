FROM tiangolo/uwsgi-nginx:python3.10

# Install requirements
COPY ./Flask/requirements.txt /tmp/requirements.txt
RUN pip install --no-cache-dir -r /tmp/requirements.txt