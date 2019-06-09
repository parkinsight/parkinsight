from flask import Blueprint, request, make_response, jsonify
from ParkinsightServer import bcrypt
from .models import db, User, BlacklistToken

bp = Blueprint(__name__, 'routes')

def get_user_from_auth_token(request):
    auth_header = request.headers.get('Authorization')
    auth_token = ''
    if auth_header:
        try:
            auth_token = auth_header.split(" ")[1]
        except IndexError:
            return null
    else:
        return null
    if auth_token:
        resp = User.decode_auth_token(auth_token)
    if not isinstance(resp, str):    
        user = User.query.filter_by(id=resp).first()
        return user

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


@bp.route('/user/scores', methods=['GET'])
def get_scores():
    """Score Endpoint."""
    user = get_user_from_auth_token(request)
    print(user.email)
    # user = User.query.filter_by(email='email').first()
    scores = []
    if user.scores:
        for x in user.scores:
            scores.append({'score': x.score, 'date': x.date})
        responseObject = {
        'scores': scores
        }
        return make_response(jsonify(responseObject)), 200
    else:
        responseObject = {
            'scores': scores
        }
        return make_response(jsonify(responseObject)), 200

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


# TODO: hook this up to the model.. should we be saving the wav file? 
# delete the wav file from disk after
@bp.route('/user/voicerecording', methods=['POST'])
def user_voiceRecording():
    """User Voice Recording Endpoint."""
    user = get_user_from_auth_token(request)
    if not 'file' in request.files:
            return make_response(jsonify({'error': 'no file'})), 400
    f = request.files['file']
    f.save(f.filename)
    print(f.filename)
    responseObject = {
        'status': f.filename
    }
    return make_response(jsonify(responseObject)), 200