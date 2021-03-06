# PARKINSIGHT
By analyzing speech and sensor data with machine learning, ParkInsight assesses symptoms of Parkinson's disease by using the standard UPDRS scale.

Built with Android, a Flask server backend and a TensorFlow model deployed using Azure Machine Learning Service.

<p float="left">
  <img src="img/login.png" width="250" style="margin-right:1000px" />
  <img src="img/voice_test.png" width="250"; style="margin-right:1000px" />
  <img src="img/dashboard.png" width="250"/>
</p>

### Acknowledgements
[UCI Parkinsons Telemonitoring Data Set](https://archive.ics.uci.edu/ml/datasets/parkinsons+telemonitoring) <br>
A Tsanas, MA Little, PE McSharry, LO Ramig (2009)
'Accurate telemonitoring of Parkinson’s disease progression by non-invasive speech tests',
IEEE Transactions on Biomedical Engineering


## Developer Installation


[pipenv](https://pipenv.readthedocs.io/en/latest/) - for dependency management.

```bash
pip install pipenv
pipenv install --dev
```

`pipenv` will manage a [virtualenv](https://virtualenv.pypa.io/en/stable/),
so interacting with the program or using the development tools has to be done
through pipenv, like so:

```bash
pipenv run <some commmand>
```

This can get inconvenient, so you can instead create a shell that runs in the managed
environment like so:

```bash
pipenv shell
```

and then commands can be run like normal.

## Database

using postgres for now - [installation for ubuntu 18.04](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-postgresql-on-ubuntu-18-04)

for flask-sqlalchemy (an ORM) to work on ubuntu a few extra libraries need to be installed (if psycopg2 doesn't install using apt-get, use pip):

```bash
sudo apt-get install psycopg2 libpq-dev python-dev
```

export some environment variables for the database connection string

```bash
export POSTGRES_URL="127.0.0.1:5432"
export POSTGRES_USER="postgres"
export POSTGRES_PW="root"
export POSTGRES_DB="parkinsight"
export FLASK_APP="ParkinsightServer"
```


Finally run the following command to create the database. Using this command will delete existing data.

```bash
flask resetdb
```

## Server

To start the server:

```
$ flask run --host=0.0.0.0
  Running on http://127.0.0.1:5000/
```
