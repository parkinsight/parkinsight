from flask import Blueprint, request, make_response, jsonify
from ParkinsightServer import bcrypt
from .models import db, User, BlacklistToken

bp = Blueprint(__name__, 'routes')

@bp.route('/', methods=['GET'])
def status():
    """Status endpoint."""
    password = bcrypt.generate_password_hash("yesa", 4).decode()
    responseObject = {
        'token': password
    }
    return make_response(jsonify(responseObject))

@bp.route('/register', methods=['POST'])
def register():
    """Registration endpoint."""
     # get the post data
    post_data = request.get_json()
    # check if user already exists
    user = User.query.filter_by(email=post_data.get('email')).first()
    if not user:
        try:
            user = User(
                email=post_data.get('email'),
                password=post_data.get('password')
            )
            # insert the user
            db.session.add(user)
            db.session.commit()
            # generate the auth token
            auth_token = user.encode_auth_token(user.id)
            responseObject = {
                'status': 'success',
                'message': 'Successfully registered.',
                'auth_token': auth_token.decode()
            }
            return make_response(jsonify(responseObject)), 201
        except Exception as e:
            responseObject = {
                'status': 'fail',
                'message': 'Some error occurred. Please try again.'
            }
            return make_response(jsonify(responseObject)), 401
    else:
        responseObject = {
            'status': 'fail',
            'message': 'User already exists. Please Log in.',
        }
        return make_response(jsonify(responseObject)), 202


@bp.route('/login', methods=['POST'])
def login():
    """Login Endpoint."""
    # get the post data
    post_data = request.get_json()
    print('email: ' + post_data.get('email'))
    print('pass: ' + post_data.get('password'))
    try:
        # fetch the user data
        user = User.query.filter_by(
            email=post_data.get('email')
        ).first()
        if user and bcrypt.check_password_hash(
            user.password, post_data.get('password')
        ):
            auth_token = user.encode_auth_token(user.id)
            if auth_token:
                responseObject = {
                    'status': 'success',
                    'message': 'Successfully logged in.',
                    'auth_token': auth_token.decode()
                }
                return make_response(jsonify(responseObject)), 200
        else:
            print ("made failure")
            responseObject = {
                'status': 'fail',
                'message': 'User does not exist.'
            }
            return make_response(jsonify(responseObject)), 404
    except Exception as e:
        print("there was an exception")
        responseObject = {
            'status': 'fail',
            'message': 'Try again'
        }
        return make_response(jsonify(responseObject)), 500

@bp.route('/user/voicerecording', methods=['POST'])
def user_voiceRecording():
    """User Voice Recording Endpoint."""
    if not 'file' in request.files:
            return make_response(jsonify({'error': 'no file'})), 400
    f = request.files['file']
    f.save(f.filename)
    print(f.filename)
    responseObject = {
        'status': f.filename
    }
    return make_response(jsonify(responseObject)), 200

@bp.route('/user', methods=['GET'])
def user_resource():
    """User Resource Endpoint."""
    # get the auth token
    auth_header = request.headers.get('Authorization')
    if auth_header:
        try:
            auth_token = auth_header.split(" ")[1]
        except IndexError:
            responseObject = {
                'status': 'fail',
                'message': 'Bearer token malformed.'
            }
            return make_response(jsonify(responseObject)), 401
    else:
        auth_token = ''
    if auth_token:
        resp = User.decode_auth_token(auth_token)
        if not isinstance(resp, str):
            user = User.query.filter_by(id=resp).first()
            responseObject = {
                'status': 'success',
                'data': {
                    'user_id': user.id,
                    'email': user.email,
                    'admin': user.admin,
                    'registered_on': user.registered_on
                }
            }
            return make_response(jsonify(responseObject)), 200
        responseObject = {
            'status': 'fail',
            'message': resp
        }
        return make_response(jsonify(responseObject)), 401
    else:
        responseObject = {
            'status': 'fail',
            'message': 'Provide a valid auth token.'
        }
        return make_response(jsonify(responseObject)), 401