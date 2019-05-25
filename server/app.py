import os
from flask import Flask
from .routes import bp
from .models import db


def create_app(config=None):
    app = Flask(__name__)

    # # load default configuration
    # app.config.from_object('website.settings')

    # load environment configuration
    if 'WEBSITE_CONF' in os.environ:
        app.config.from_envvar('WEBSITE_CONF')

    # load app sepcified configuration
    if config is not None:
        if isinstance(config, dict):
            app.config.update(config)
        elif config.endswith('.py'):
            app.config.from_pyfile(config)

    setup_app(app)
    return app


def setup_app(app):
    db.init_app(app)
    app.register_blueprint(bp, url_prefix='')