import os, sys
import numpy as np
import pandas as pd
import parselmouth
from parselmouth.praat import call
from sklearn.externals import joblib

class model():
    
    def __init__(self, model_path = 'analysis/ridge_model.pkl'):
        self.model = joblib.load(model_path)

    def predict(self, audio):
        sound = parselmouth.Sound(audio)
        output = model.measurePitch(sound, 75, 400, "Hertz")
        output = pd.DataFrame(output).T
        prediction = self.model.predict(output)
        print(prediction)

    @staticmethod
    def measurePitch(sound, f0min, f0max, unit):

        duration = call(sound, "Get total duration") # duration
        pitch = call(sound, "To Pitch", 0.0, f0min, f0max) # create a praat pitch object
        meanF0 = call(pitch, "Get mean", 0, 0, unit) # get mean pitch
        stdevF0 = call(pitch, "Get standard deviation", 0 ,0, unit) # get standard deviation
        harmonicity = call(sound, "To Harmonicity (cc)", 0.01, f0min, 0.1, 1.0)
        hnr = call(harmonicity, "Get mean", 0, 0)
        pointProcess = call(sound, "To PointProcess (periodic, cc)", f0min, f0max)
        localJitter = call(pointProcess, "Get jitter (local)", 0, 0, 0.0001, 0.02, 1.3)
        localabsoluteJitter = call(pointProcess, "Get jitter (local, absolute)", 0, 0, 0.0001, 0.02, 1.3)
        rapJitter = call(pointProcess, "Get jitter (rap)", 0, 0, 0.0001, 0.02, 1.3)
        ppq5Jitter = call(pointProcess, "Get jitter (ppq5)", 0, 0, 0.0001, 0.02, 1.3)
        ddpJitter = call(pointProcess, "Get jitter (ddp)", 0, 0, 0.0001, 0.02, 1.3)
        localShimmer =  call([sound, pointProcess], "Get shimmer (local)", 0, 0, 0.0001, 0.02, 1.3, 1.6)
        localdbShimmer = call([sound, pointProcess], "Get shimmer (local_dB)", 0, 0, 0.0001, 0.02, 1.3, 1.6)
        apq3Shimmer = call([sound, pointProcess], "Get shimmer (apq3)", 0, 0, 0.0001, 0.02, 1.3, 1.6)
        aqpq5Shimmer = call([sound, pointProcess], "Get shimmer (apq5)", 0, 0, 0.0001, 0.02, 1.3, 1.6)
        apq11Shimmer =  call([sound, pointProcess], "Get shimmer (apq11)", 0, 0, 0.0001, 0.02, 1.3, 1.6)
        ddaShimmer = call([sound, pointProcess], "Get shimmer (dda)", 0, 0, 0.0001, 0.02, 1.3, 1.6)

        return localJitter, localabsoluteJitter, rapJitter, ppq5Jitter, ddpJitter, localShimmer, localdbShimmer, apq3Shimmer, aqpq5Shimmer, apq11Shimmer, ddaShimmer, hnr
    
