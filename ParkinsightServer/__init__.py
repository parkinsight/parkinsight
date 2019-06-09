import os
from flask import Flask
from flask_bcrypt import Bcrypt # god I doubt this will work
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy
import sqlalchemy
from flask.cli import with_appcontext

def get_env_variable(name):
    try:
        return os.environ[name]
    except KeyError:
        message = "Expected environment variable '{}' not set.".format(name)
        raise Exception(message) 

def create_app(config=None):
    app = Flask(__name__)

    # load environment configuration
    if 'WEBSITE_CONF' in os.environ:
        app.config.from_envvar('WEBSITE_CONF')

    # load app sepcified configuration
    if config is not None:
        if isinstance(config, dict):
            app.config.update(config)
        elif config.endswith('.py'):
            app.config.from_pyfile(config)
    return app

POSTGRES_URL = get_env_variable("POSTGRES_URL")
POSTGRES_USER = get_env_variable("POSTGRES_USER")
POSTGRES_PW = get_env_variable("POSTGRES_PW")
POSTGRES_DB = get_env_variable("POSTGRES_DB")

DB_URL = 'postgresql+psycopg2://{user}:{pw}@{url}/{db}?sslmode=require'.format(user=POSTGRES_USER,pw=POSTGRES_PW,url=POSTGRES_URL,db=POSTGRES_DB)

app = create_app({
    'SECRET_KEY': 'secret',
    'SQLALCHEMY_TRACK_MODIFICATIONS': False,
    'SQLALCHEMY_DATABASE_URI': DB_URL,
    'BCRYPT_LOG_ROUNDS': 4
})

bcrypt = Bcrypt(app)
CORS(app)

@app.cli.command()
def initdb():
    from .models import db
    seed()

@app.cli.command('resetdb')
def resetdb_command():
    """Destroys and creates the database + tables."""

    from sqlalchemy_utils import database_exists, create_database, drop_database
    if database_exists(DB_URL):
        print('Deleting database.')
        drop_database(DB_URL)
    if not database_exists(DB_URL):
        print('Creating database.')
        create_database(DB_URL)

    print('Creating tables.')
    db.create_all()
    print('Shiny!')

@with_appcontext
def seed():
    from .models import User, TestScore
    user = User(email='email2',password='password')
    db.session.add(user)
    db.session.commit()

    user = User.query.filter_by(email='email').first()
    score1 = TestScore(score=23,user_id=user.id,date='2019-05-01 13:23:44')
    score2 = TestScore(score=44, user_id=user.id, date='2019-05-03 13:23:44')
    score3 = TestScore(score=66, user_id=user.id, date='2019-05-24 13:23:44')

    db.session.add_all([score1, score2, score3])
    db.session.commit()
    print('database seeded')
    
from .routes import bp
app.register_blueprint(bp, url_prefix='')

from .models import db
db.init_app(app)