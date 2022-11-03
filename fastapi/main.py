from fastapi import FastAPI, Response, status
from fastapi.params import Depends
from sqlalchemy import desc
from sqlalchemy.orm import Session
from apscheduler.schedulers.background import BackgroundScheduler
import function
import models
import schemas
from typing import List
from Connection import SessionLocal, engine
from finance import finance_create

# models.Base.metadata.create_all(bind=engine)
models.Base.metadata.bind = engine
app = FastAPI()
s = BackgroundScheduler(timezone='Asia/Seoul')
s.add_job(finance_create, 'cron', [engine], hour='9', minute='10')
s.start()


def get_db():
    try:
        db = SessionLocal()
        yield db
    finally:
        db.close()


@app.post("/data/v1/user/register", status_code=200)
async def test(user: schemas.userReq, response: Response, db: Session = Depends(get_db)):
    try:
        user_id = db.query(models.User).filter(models.User.phone == user.phone).first().id
        function.create(user_id, db)

    except:
        response.status_code = status.HTTP_409_CONFLICT


@app.get("/data/v1/finance", response_model=List[schemas.FinanceOut], status_code=200)
async def finance(db: Session = Depends(get_db)):
    a = db.query(models.Finance).filter(models.Finance.fn_name == '기아').order_by(desc(models.Finance.fn_date)).first().fn_date
    return db.query(models.Finance).filter(models.Finance.fn_date == a).all()


@app.get("/data/v1/finance/{fn_name}", response_model=List[schemas.FinanceOut], response_model_exclude={"fn_per"}, status_code=200)
async def finance_detail(fn_name: str, db: Session = Depends(get_db)):
    return db.query(models.Finance).filter(models.Finance.fn_name == fn_name).all()