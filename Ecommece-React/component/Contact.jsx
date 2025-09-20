import React from 'react'

const Contact = () => {
    return (
        <div>
            <div className='container mb-5'>
                <div className='row'>
                    <div className='col-12 text-center py-4 my-4'>
                        <h1> Have Some Qn ?</h1>
                        <hr />
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-5 d-flex justify-content-center">
                        <img src="/assets/contactus.jpg" alt="Contact us " height="300px" width="300px" />
                    </div>
                    <div className="col-md-6">
                        <form>
                            <div class="mb-3">
                                <label for="exampleForm" class="form-label">Null Name </label>
                                <input type="text" class="form-control" name="" id="exampleForm" placeholder='Ambrish Durani'/>
                            </div>
                            <div class="mb-3">
                                <label for="exampleFormControlInput1" class="form-label"> Email Address </label>
                                <input type="email" className='form-control' id="exampleFormControlInput1" placeholder='name@example.com' />
                            </div>
                            <div class="mb-3">
                                <label for="exampleFormControl1Textarea1" class="form-label">Message</label>
                                <textarea class="form-control" id="exampleFormControlTextarea1" rows="5"></textarea>
                            </div>
                            <button type="submit" class="btn btn-outline-primary"> Send Message </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Contact
