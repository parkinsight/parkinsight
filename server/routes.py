from flask import Blueprint, request, jsonify

bp = Blueprint(__name__, 'routes')

@bp.route('/', methods=['GET'])
def status():
    """Status endpoint."""
    return "it's up!"